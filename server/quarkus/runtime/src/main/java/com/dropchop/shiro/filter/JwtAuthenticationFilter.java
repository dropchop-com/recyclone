package com.dropchop.shiro.filter;

import com.dropchop.recyclone.base.api.config.JwtConfig;
import com.dropchop.recyclone.base.api.service.security.JwtService;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.shiro.token.JwtShiroToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
@SuppressWarnings("unused")
public class JwtAuthenticationFilter extends HeaderHttpAuthenticationFilter {

  public static final String JWT_AUTHENTICATED_REQUEST_CLAIMS = "::jwtAuthenticatedRequestClaims::";
  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtConfig jwtConfig;

  @Inject
  Subject subject;

  @Inject
  JwtService jwtService;

  @Inject
  public JwtAuthenticationFilter(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
    setAuthcScheme(BEARER);
    setAuthzScheme(BEARER);
  }

  public Subject getSubject() {
    return subject;
  }

  private String extractAuthorizationFormField(String body) {
    MultivaluedMap<String, String> form = new MultivaluedHashMap<>();
    for (String pair : body.split("&")) {
      if (pair.isEmpty()) {
        continue;
      }
      String[] kv = pair.split("=", 2);
      if (kv.length < 1) {
        continue;
      }
      String k = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
      String v = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
      form.add(k.toLowerCase(), v); // lower case to avoid case-sensitive matching
    }

    return form.getFirst("authorization");
  }

  @Override
  protected boolean isLoginAttempt(ContainerRequestContext ctx) {
    if (!super.isLoginAttempt(ctx)) {
      return ctx.getMediaType() != null &&
          MediaType.APPLICATION_FORM_URLENCODED_TYPE.isCompatible(ctx.getMediaType());
    }
    return true;
  }

  @Override
  protected AuthenticationToken createToken(ContainerRequestContext ctx) {
    String authorizationHeaderContent = getAuthzHeader(ctx);
    if (authorizationHeaderContent == null || authorizationHeaderContent.isBlank()) {
      if (ctx.getMediaType() == null ||
          !MediaType.APPLICATION_FORM_URLENCODED_TYPE.isCompatible(ctx.getMediaType())) {
        return super.createBearerToken("", ctx);
      }
      // form post usually can't set headers, so we extract the token from the form body
      try {
        byte[] bytes;
        try (InputStream in = ctx.getEntityStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
          in.transferTo(out);
          bytes = out.toByteArray();
        }
        String body = new String(bytes, StandardCharsets.UTF_8);
        // put back the original stream
        ctx.setEntityStream(new ByteArrayInputStream(bytes));
        authorizationHeaderContent = extractAuthorizationFormField(body);
      } catch (Exception e) {
        log.warn("Unable to drain original stream", e);
        return super.createBearerToken("", ctx);
      }
      if (authorizationHeaderContent == null || authorizationHeaderContent.isBlank()) {
        return super.createBearerToken("", ctx);
      }
    }

    log.trace("Attempting to execute login with auth header");
    final String[] principalsAndCredentials = getPrincipalsAndCredentials(authorizationHeaderContent, ctx);
    final String encodedToken = principalsAndCredentials[0];
    try {
      Claims claims = jwtService.decode(this.jwtConfig, encodedToken);
      if (claims == null) {
        return super.createBearerToken("", ctx);
      }
      String jwtSubjectString = claims.getSubject();
      if (jwtSubjectString == null) {
        return super.createBearerToken("", ctx);
      }
      final User user = new User();
      user.setId(jwtSubjectString);
      ctx.setProperty(JWT_AUTHENTICATED_REQUEST_CLAIMS, claims);
      return new JwtShiroToken(user, this.jwtConfig.getIssuer(), encodedToken, true);
    } catch (MalformedJwtException jwtEx) {
      log.warn("Invalid JWT: {}",principalsAndCredentials[0], jwtEx);
      return createBearerToken("", ctx);
    }
  }
}