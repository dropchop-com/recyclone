package com.dropchop.recyclone.base.es.repo.config;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/25/25.
 */
public interface HasRootAlias extends ClassIndexConfig {
  default String getRootAlias() {
    return getDefaultIndexName();
  }
}
