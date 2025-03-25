package com.dropchop.recyclone.base.api.repo.config;

import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/25/25.
 */
@SuperBuilder
@SuppressWarnings("unused")
public class ClassStaticIndexConfig extends DefaultIndexConfig implements HasStaticWriteIndex {
  @Override
  public String getWriteIndex() {
    return getDefaultIndexName();
  }
}
