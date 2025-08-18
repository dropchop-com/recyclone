package com.dropchop.shiro.jaxrs;

import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 12. 21.
 */
public class ShiroRequestStateFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private static final Logger log = LoggerFactory.getLogger(ShiroRequestStateFilter.class);

  private final ShiroAuthorizationService authorizationService;

  public ShiroRequestStateFilter(ShiroAuthorizationService authorizationService) {
    this.authorizationService = authorizationService;
  }

  @Override
  public void filter(ContainerRequestContext context) {
    this.authorizationService.bindSubject(context);
    log.trace("Initialized Shiro state [{}].", context.getUriInfo().getPath());
  }

  @Override
  public void filter(ContainerRequestContext reqContext, ContainerResponseContext respContext) {
    this.authorizationService.unbindSubject(reqContext);
    log.trace("Initialized Shiro state [{}].", reqContext.getUriInfo().getPath());
  }
}
