package com.dropchop.recyclone.base.es.repo.config;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/27/25.
 */
@Getter
@SuperBuilder
@SuppressWarnings("unused")
public class StaticIndexConfig
    implements ClassIndexConfig, HasRootAlias, HasStaticReadIndex, HasStaticWriteIndex {

  private final Class<?> rootClass;
  private final String indexName;

  @Override
  public String getReadIndex() {
    return this.indexName;
  }

  @Override
  public String getWriteIndex() {
    return this.indexName;
  }
}
