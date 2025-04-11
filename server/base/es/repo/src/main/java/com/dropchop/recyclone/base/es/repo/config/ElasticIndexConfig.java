package com.dropchop.recyclone.base.es.repo.config;

public interface ElasticIndexConfig {
  default Integer getSizeOfPagination() {
    return 10000;
  }
}
