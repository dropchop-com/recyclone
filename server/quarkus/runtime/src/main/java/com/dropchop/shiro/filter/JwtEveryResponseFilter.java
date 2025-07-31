package com.dropchop.shiro.filter;

import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig.Rest.Security.JwtConfig;
import com.dropchop.shiro.token.JwtHelper;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import org.apache.shiro.subject.Subject;

public class JwtEveryResponseFilter extends BearerHttpAuthenticationFilter implements ResponseFilter {

  private final JwtConfig jwtConfig;

  public JwtEveryResponseFilter(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
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