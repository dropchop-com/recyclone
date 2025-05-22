package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;
import com.dropchop.recyclone.base.es.repo.config.ElasticIndexConfig;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public interface ElasticQueryBuilder {

  @Getter
  class ValidationData {
    private final List<String> rootFields = new ArrayList<>();
    private Condition rootCondition = null;

    protected void setRootCondition(Condition rootCondition) {
      if (rootCondition == null) {
        return;
      }
      this.rootCondition = rootCondition;
    }

    protected void addRootField(String candidateField, Condition parentCondition) {
      if (this.rootCondition == parentCondition) {
        this.rootFields.add(candidateField);
      }
    }
  }


  boolean useSearchAfter(ElasticIndexConfig indexConfig, QueryParams params);
  QueryNodeObject build(ValidationData validationData, ElasticIndexConfig indexConfig, QueryParams params);
  QueryNodeObject build(ValidationData validationData, QueryParams params);
  QueryNodeObject buildAggregation(Aggregation aggregation);

}
