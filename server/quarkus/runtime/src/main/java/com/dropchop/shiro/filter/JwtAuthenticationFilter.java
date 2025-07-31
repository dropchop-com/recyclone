package com.dropchop.shiro.filter;

import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig.Rest.Security.JwtConfig;
import com.dropchop.shiro.token.JwtHelper;
import com.dropchop.shiro.token.JwtShiroToken;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtAuthenticationFilter extends BearerHttpAuthenticationFilter implements ResponseFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtConfig jwtConfig;

  public JwtAuthenticationFilter(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  @Override
  protected AuthenticationToken createToken(ContainerRequestContext request) {
    String authorizationHeaderContent = getAuthzHeader(request);
    if (authorizationHeaderContent == null || authorizationHeaderContent.isBlank()) {
      return super.createBearerToken("", request);
    }

    log.trace("Attempting to execute login with auth header");
    final String[] principalsAndCredentials = getPrincipalsAndCredentials(authorizationHeaderContent, request);
    final String encodedToken = principalsAndCredentials[0];
    try {
      String subject = JwtHelper.decodeSubject(this.jwtConfig, encodedToken);
      if (subject == null) {
        return super.createBearerToken("", request);
      }
      final User user = new User();
      user.setId(subject);
      return new JwtShiroToken(user, this.jwtConfig.issuer, encodedToken, true);
    } catch (MalformedJwtException jwtEx) {
      log.warn("Invalid JWT: {}",principalsAndCredentials[0], jwtEx);
      return createBearerToken("", request);
    }
  }

  @Override
  public boolean onFilterResponse(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
    String authorizationHeaderContent = getAuthzHeader(requestContext);
    if (authorizationHeaderContent == null || authorizationHeaderContent.isBlank()) {
      return true;
    }
    Subject subject = getSubject();
    Object principal = subject.getPrincipal();
    if (principal instanceof User user) {
      String newToken = JwtHelper.encode(this.jwtConfig, user);
      responseContext.getHeaders().add("X-Auth-Token", newToken);
      responseContext.getHeaders().add("Access-Control-Expose-Headers", "*");
    }
    return true;
  }
}