package com.dropchop.recyclone.quarkus.runtime.rest.jaxrs;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;

/**
 * Sets default request Accept content type to "application/json; charset=UTF-8" if missing or wildcard Accept header.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 01. 22.
 */
@PreMatching
@ConstrainedTo(RuntimeType.SERVER)
@SuppressWarnings("unused")
public class ContentTypeFilter implements ContainerRequestFilter {

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
