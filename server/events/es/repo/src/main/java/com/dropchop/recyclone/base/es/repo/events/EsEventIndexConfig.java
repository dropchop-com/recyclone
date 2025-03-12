package com.dropchop.recyclone.base.es.repo.events;

import com.dropchop.recyclone.base.api.repo.config.DateBasedIndexConfig;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApplicationScoped
public class EsEventIndexConfig implements DateBasedIndexConfig {
  private String ingestPipeline = "event_index_ingest_pipeline";
  private Integer sizeOfPagination = 10000;
  private String alias = "event";
}
