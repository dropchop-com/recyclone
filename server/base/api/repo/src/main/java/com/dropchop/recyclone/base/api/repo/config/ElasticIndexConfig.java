package com.dropchop.recyclone.base.api.repo.config;

public interface ElasticIndexConfig extends ClassIndexConfig {
  default Integer getSizeOfPagination() {
    return 10000;
  }
}
