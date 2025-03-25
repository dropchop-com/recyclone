package com.dropchop.recyclone.base.api.repo.config;

import com.dropchop.recyclone.base.api.model.base.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/25/25.
 */
@SuppressWarnings("unused")
public interface HasEntityBasedWriteIndex {
  <E extends Entity> String getWriteIndex(E entity);
}
