package com.dropchop.shiro.filter;

import com.dropchop.recyclone.base.api.config.JwtConfig;
import com.dropchop.recyclone.base.api.model.security.jwt.JwtClaims;
import com.dropchop.recyclone.base.api.service.security.JwtService;
import com.dropchop.recyclone.base.dto.model.security.User;
import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import org.apache.shiro.subject.Subject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.dropchop.shiro.filter.JwtAuthenticationFilter.JWT_AUTHENTICATED_REQUEST_CLAIMS;

@ApplicationScoped
public class JwtEveryResponseFilter extends HeaderHttpAuthenticationFilter implements ResponseFilter {

  private final JwtConfig jwtConfig;

  @Inject
  Subject subject;

  @Inject
  JwtService jwtService;

  @Inject
  public JwtEveryResponseFilter(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
    setAuthcScheme(BEARER);
    setAuthzScheme(BEARER);
  }

  public Subject getSubject() {
    return subject;
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
      Object oClaims = requestContext.getProperty(JWT_AUTHENTICATED_REQUEST_CLAIMS);
      long timeout = jwtConfig.getTimeoutSeconds();
      Map<String, Object> responseClaims = new HashMap<>();
      if (oClaims instanceof Claims sourceClaims) {
        Date expiration = sourceClaims.getExpiration();
        Date issuedAt = sourceClaims.getIssuedAt();
        timeout = ((expiration.getTime() - issuedAt.getTime()) / 1000);
        String grantType = sourceClaims.get(JwtClaims.grant_type.name(), String.class);
        if (grantType != null && !grantType.isBlank()) {
          responseClaims.put(JwtClaims.grant_type.name(), grantType);
        }
      }
      String newToken = jwtService.encode(this.jwtConfig, timeout, user.getId(), responseClaims);
      responseContext.getHeaders().add("X-Auth-Token", newToken);
      responseContext.getHeaders().add("Access-Control-Expose-Headers", "*");
    }
    return true;
  }
}