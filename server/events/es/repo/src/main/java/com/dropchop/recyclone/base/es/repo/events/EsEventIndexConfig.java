package com.dropchop.recyclone.base.es.repo.events;

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
    return "event-ingest";
  }

  @Override
  public Integer getSizeOfPagination() {
    return 100;
  }

  @Override
  public <E extends EsEntity> String getIndexName(E entity) {
    return entity.getClass().getSimpleName();
  }
}
