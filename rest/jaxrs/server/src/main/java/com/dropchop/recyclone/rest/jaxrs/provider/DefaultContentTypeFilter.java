package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.rest.jaxrs.api.MediaType;

import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

/**
 * Sets default request Accept content type to "application/json; charset=UTF-8" if missing or wildcard Accept header.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 01. 22.
 */
@ConstrainedTo(RuntimeType.SERVER)
public class DefaultContentTypeFilter implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext ctx) {
    String ctp = ctx.getHeaderString("Accept");
    if (ctp == null) {
      ctx.getHeaders().putSingle("Accept", "application/json; charset=UTF-8");
    }
    if (MediaType.WILDCARD.equals(ctp)) {
      ctx.getHeaders().putSingle("Accept", "application/json; charset=UTF-8");
    }
  }
}
