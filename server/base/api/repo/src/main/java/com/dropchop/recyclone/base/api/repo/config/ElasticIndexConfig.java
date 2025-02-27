package com.dropchop.recyclone.base.api.repo.config;

import com.dropchop.recyclone.base.api.model.utils.Strings;
import com.dropchop.recyclone.base.es.model.base.EsEntity;

public interface ElasticIndexConfig {
  String getIngestPipeline();
  String getAlias();
  Integer getSizeOfPagination();

  default <S extends EsEntity> String getIndexName(S entity) {
    return getIndexName(entity.getClass());
  }

  default String getIndexName(Class<?> clazz) {
    String simpleName = clazz.getSimpleName();
    if (simpleName.startsWith("Es")) {
      simpleName = simpleName.substring(2);
    }
    return Strings.toSnakeCase(simpleName);
  }

  default String getRootClassName(Class<?> clazz) {
    String simpleName = clazz.getSimpleName();
    if (simpleName.startsWith("Es")) {
      simpleName = simpleName.substring(2);
    }
    return Strings.toSnakeCase(simpleName);
  }

}
