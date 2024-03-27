package com.dropchop.shiro.jaxrs;

import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 12. 21.
 */
public class ShiroThreadStateFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private static final Logger log = LoggerFactory.getLogger(ShiroThreadStateFilter.class);

  private final ShiroAuthorizationService authorizationService;

  public ShiroThreadStateFilter(ShiroAuthorizationService authorizationService) {
    this.authorizationService = authorizationService;
    log.debug("Constructed {}.", this.getClass().getName());
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    this.authorizationService.bindSubjectToThreadStateInRequestContext(requestContext);
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
    this.authorizationService.unbindSubjectFromThreadStateInRequestContext(requestContext);
  }
}
