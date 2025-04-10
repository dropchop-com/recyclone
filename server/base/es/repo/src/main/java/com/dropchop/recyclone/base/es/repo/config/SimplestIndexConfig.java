package com.dropchop.recyclone.base.es.repo.config;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/27/25.
 */
@Getter
@SuperBuilder
@SuppressWarnings("unused")
public class SimplestIndexConfig
    implements ElasticIndexConfig, HasStaticReadIndex, HasStaticWriteIndex {

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
