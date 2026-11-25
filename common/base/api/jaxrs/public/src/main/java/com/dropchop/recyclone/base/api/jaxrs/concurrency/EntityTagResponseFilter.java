package com.dropchop.recyclone.base.api.jaxrs.concurrency;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.EntityTag;

import java.util.function.Supplier;

import static jakarta.ws.rs.core.HttpHeaders.ETAG;

public class EntityTagResponseFilter implements ContainerResponseFilter {

  private final Supplier<EntityTag> entityTag;

  public EntityTagResponseFilter(Supplier<EntityTag> entityTag) {
    this.entityTag = entityTag;
  }

  public static void writeEntityTag(ContainerResponseContext response, EntityTag entityTag) {
    int status = response.getStatus();
    if (entityTag != null && status >= 200 && status < 300 && !response.getHeaders().containsKey(ETAG)) {
      response.getHeaders().putSingle(ETAG, entityTag);
    }
  }

  @Override
  public void filter(ContainerRequestContext request, ContainerResponseContext response) {
    writeEntityTag(response, entityTag.get());
  }
}
