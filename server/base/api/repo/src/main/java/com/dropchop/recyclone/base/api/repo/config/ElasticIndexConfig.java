package com.dropchop.recyclone.base.api.repo.config;

import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.utils.Strings;
import com.dropchop.recyclone.base.es.model.base.EsEntity;

import java.util.Date;

public interface ElasticIndexConfig {
  String getIngestPipeline();

  Integer getSizeOfPagination();

  <E extends EsEntity> String getIndexName(E entity);

  default String getRootClassName(Class<?> clazz) {
    String simpleName = clazz.getSimpleName();
    if (simpleName.startsWith("Es")) {
      simpleName = simpleName.substring(2);
    }
    return Strings.toSnakeCase(simpleName);
  }

  default String getDefaultIndexName(Class<?> clazz) {
    String simpleName = clazz.getSimpleName();
    if (simpleName.startsWith("Es")) {
      simpleName = simpleName.substring(2);
    }
    return Strings.toSnakeCase(simpleName);
  }

  default String getMonthBasedIndexName(HasCreated entity) {
    String entityName = entity.getClass().getSimpleName().toLowerCase();
    int month = entity.getCreated().getMonthValue();
    return entityName + "-" + entity.getCreated().getYear() + "-" + (month < 10 ? "0" + month : month)  + "-01";
  }
}
