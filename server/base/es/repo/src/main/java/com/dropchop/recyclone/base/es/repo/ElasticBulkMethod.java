package com.dropchop.recyclone.base.es.repo;

public interface ElasticBulkMethod {
  <S> String getIndexName(S entity);
}
