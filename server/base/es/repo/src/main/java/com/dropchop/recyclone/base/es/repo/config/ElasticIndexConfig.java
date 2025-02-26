package com.dropchop.recyclone.base.es.repo.config;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
@NoArgsConstructor
public class ElasticIndexConfig implements com.dropchop.recyclone.base.api.repo.config.DefaultIndexConfig {
  private String ingestPipeline = null;
  private Integer sizeOfPagination = 10000;
}
