package com.dropchop.recyclone.base.api.repo.config;

import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;

@SuperBuilder
public class DefaultIndexConfig
    implements ElasticIndexConfig, HasIngestPipeline, HasRootAlias, HasAliases, HasPrefix, HasClassBasedDefaultSort {

  private final Class<?> rootClass;
  private final String ingestPipeline;
  private final Collection<String> aliases = new ArrayList<>();
  private final String prefix;

  @Override
  public Collection<String> getAliases() {
    return this.aliases;
  }

  @Override
  public Class<?> getRootClass() {
    return rootClass;
  }

  @Override
  public String getPrefix() {
    return prefix;
  }

  @Override
  public String getIngestPipeline() {
    return this.ingestPipeline;
  }

  @Override
  public Integer getSizeOfPagination() {
    return 10000;
  }
}
