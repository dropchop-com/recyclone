package com.dropchop.recyclone.base.api.service;

import com.dropchop.recyclone.base.api.model.base.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 08. 04. 2026.
 */
public interface Authorizer<E extends Entity> {
  boolean mustAuthorize();
  boolean authorize(E entity);
}
