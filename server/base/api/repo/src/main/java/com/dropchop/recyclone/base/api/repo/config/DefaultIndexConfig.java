package com.dropchop.recyclone.base.api.repo.config;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@SuperBuilder
public class DefaultIndexConfig
    implements ElasticIndexConfig, HasRootAlias, HasAliases, HasPrefix, HasClassBasedDefaultSort {

  private final Class<?> rootClass;
  private final Collection<String> aliases = new ArrayList<>();
  private final String prefix;
}
