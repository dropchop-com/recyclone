package com.dropchop.recyclone.base.es.repo.config;

import com.dropchop.recyclone.base.api.model.utils.Strings;
import com.dropchop.recyclone.base.es.model.base.EsEntity;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
@NoArgsConstructor
public class ElasticIndexConfig implements com.dropchop.recyclone.base.api.repo.config.ElasticIndexConfig {
  private String ingestPipeline = null;
  private Integer sizeOfPagination = 10000;

  public <E extends EsEntity> String getIndexName(E entity) {
    return getDefaultIndexName(entity.getClass());
  }
}
