package com.dropchop.recyclone.base.es.repo.config;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApplicationScoped
public class DateBasedIndexConfig implements com.dropchop.recyclone.base.api.repo.config.DateBasedIndexConfig {
  private String ingestPipeline = null;
  private String alias = null;
  private Integer sizeOfPagination = 10000;
}
