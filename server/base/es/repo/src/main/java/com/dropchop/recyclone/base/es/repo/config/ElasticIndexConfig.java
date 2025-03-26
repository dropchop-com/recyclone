package com.dropchop.recyclone.base.es.repo.config;

public interface ElasticIndexConfig extends ClassIndexConfig {
  default Integer getSizeOfPagination() {
    return 10000;
  }
}
