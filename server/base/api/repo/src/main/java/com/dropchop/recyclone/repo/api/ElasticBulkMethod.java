package com.dropchop.recyclone.repo.api;

public interface ElasticBulkMethod {
  <S> String getIndexName(S entity);
}
