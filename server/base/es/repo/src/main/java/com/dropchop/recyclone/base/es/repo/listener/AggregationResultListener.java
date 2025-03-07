package com.dropchop.recyclone.base.es.repo.listener;

import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;

public interface AggregationResultListener extends RepositoryExecContextListener {
  void onAggregation(String name, Object aggregation);
}
