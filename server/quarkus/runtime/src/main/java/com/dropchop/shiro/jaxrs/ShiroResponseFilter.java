package com.dropchop.shiro.jaxrs;

import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 06. 23.
 */
public class ShiroResponseFilter implements ContainerResponseFilter {

  private final ShiroAuthorizationService authorizationService;

  public ShiroResponseFilter(ShiroAuthorizationService authorizationService) {
    this.authorizationService = authorizationService;
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
    this.authorizationService.invokeResponseFilterChain(requestContext, responseContext);
  }
}
