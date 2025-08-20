package com.dropchop.shiro.filter;

import com.dropchop.recyclone.base.api.config.JwtConfig;
import com.dropchop.recyclone.base.api.service.security.JwtService;
import com.dropchop.recyclone.base.dto.model.security.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import org.apache.shiro.subject.Subject;

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
      String newToken = jwtService.encode(this.jwtConfig, user.getId());
      responseContext.getHeaders().add("X-Auth-Token", newToken);
      responseContext.getHeaders().add("Access-Control-Expose-Headers", "*");
    }
    return true;
  }
}