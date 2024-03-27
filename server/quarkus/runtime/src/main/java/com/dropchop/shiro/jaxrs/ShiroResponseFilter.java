package com.dropchop.shiro.jaxrs;

import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 06. 23.
 */
public class ShiroResponseFilter implements ContainerResponseFilter {

  private static final Logger log = LoggerFactory.getLogger(ShiroDynamicFeature.class);

  private final ShiroAuthorizationService authorizationService;

  public ShiroResponseFilter(ShiroAuthorizationService authorizationService) {
    this.authorizationService = authorizationService;
    log.debug("Constructed {}.", this.getClass().getName());
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
    this.authorizationService.invokeResponseFilterChain(requestContext, responseContext);
  }
}
