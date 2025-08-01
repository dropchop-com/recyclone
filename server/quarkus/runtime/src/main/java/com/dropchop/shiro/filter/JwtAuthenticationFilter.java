package com.dropchop.shiro.filter;

import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.shiro.token.JwtHelper;
import com.dropchop.shiro.token.JwtShiroToken;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@SuppressWarnings("unused")
public class JwtAuthenticationFilter extends HeaderHttpAuthenticationFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtConfig jwtConfig;

  @Inject
  public JwtAuthenticationFilter(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
    setAuthcScheme(BEARER);
    setAuthzScheme(BEARER);
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
      String jwtSubjectString = JwtHelper.decodeSubject(this.jwtConfig, encodedToken);
      if (jwtSubjectString == null) {
        return super.createBearerToken("", request);
      }
      final User user = new User();
      user.setId(jwtSubjectString);
      return new JwtShiroToken(user, this.jwtConfig.issuer, encodedToken, true);
    } catch (MalformedJwtException jwtEx) {
      log.warn("Invalid JWT: {}",principalsAndCredentials[0], jwtEx);
      return createBearerToken("", request);
    }
  }
}