package com.dropchop.recyclone.quarkus.runtime.rest.jaxrs;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.EntityTag;

/** Holds the entity tag supplied by the resource for the current response. */
@RequestScoped
public class ResponseEntityTag {

  private EntityTag entityTag;

  public EntityTag get() {
    return entityTag;
  }

  public void set(EntityTag entityTag) {
    this.entityTag = entityTag;
  }
}
