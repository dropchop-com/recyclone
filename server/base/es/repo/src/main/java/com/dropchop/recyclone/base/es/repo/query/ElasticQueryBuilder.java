package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;
import com.dropchop.recyclone.base.es.repo.config.ElasticIndexConfig;

public interface ElasticQueryBuilder {

  boolean useSearchAfter(ElasticIndexConfig indexConfig, QueryParams params);

  QueryNodeObject build(QueryFieldListener fieldListener, ElasticIndexConfig indexConfig, QueryParams params);
  QueryNodeObject build(QueryFieldListener fieldListener, QueryParams params);
  QueryNodeObject build(QueryParams params);

  QueryNodeObject buildAggregation(Aggregation aggregation);
}
