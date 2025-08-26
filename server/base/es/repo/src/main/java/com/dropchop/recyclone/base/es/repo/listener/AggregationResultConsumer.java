package com.dropchop.recyclone.base.es.repo.listener;

import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;
import com.dropchop.recyclone.base.dto.model.rest.AggregationResult;

public interface AggregationResultConsumer extends RepositoryExecContextListener {
  void accept(String name, AggregationResult aggregationResult);
}
