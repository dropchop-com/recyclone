package com.dropchop.recyclone.repo.es;

public interface ElasticBulkMethod {
  <S> String getIndexName(S entity);
}
