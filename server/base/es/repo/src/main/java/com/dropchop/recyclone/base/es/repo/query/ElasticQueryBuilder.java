package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;
import com.dropchop.recyclone.base.es.repo.config.ElasticIndexConfig;

public interface ElasticQueryBuilder {

  boolean useSearchAfter(ElasticIndexConfig indexConfig, QueryParams params);

  IQueryObject build(QueryFieldListener fieldListener, ElasticIndexConfig indexConfig, QueryParams params);
  IQueryObject build(QueryFieldListener fieldListener, QueryParams params);
  IQueryObject build(QueryParams params);
  IQueryObject buildAggregation(Aggregation aggregation);
}
