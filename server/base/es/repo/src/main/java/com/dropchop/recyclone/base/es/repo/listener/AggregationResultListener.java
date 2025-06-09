package com.dropchop.recyclone.base.es.repo.listener;

import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;
import com.dropchop.recyclone.base.dto.model.rest.AggregationResult;

public interface AggregationResultListener extends RepositoryExecContextListener {
  void onAggregation(String name, AggregationResult aggregationResult);
}
