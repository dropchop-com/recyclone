package com.dropchop.recyclone.base.es.repo.events;

import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.repo.config.ElasticIndexConfig;
import com.dropchop.recyclone.base.es.model.base.EsEntity;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class EsEventIndexConfig implements ElasticIndexConfig {
  String pipelineName;

  @Override
  public String getIngestPipeline() {
    return null;
  }

  @Override
  public Integer getSizeOfPagination() {
    return 10000;
  }

  @Override
  public <E extends EsEntity> String getIndexName(E entity) {
    return getMonthBasedIndexName((HasCreated) entity);
  }
}
