package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;
import com.dropchop.recyclone.base.es.repo.config.ElasticIndexConfig;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ElasticQueryBuilder {

  @Getter
  class ValidationData {
    private final Set<String> rootFields = new HashSet<>();
    private final Set<String> knnFields = new HashSet<>();
    private Condition rootCondition;

    public void addRootField(String field, Condition parentCondition) {
      rootFields.add(field);
    }

    public void addKnnField(String field) {
      knnFields.add(field);
    }

    public Set<String> getRootFields() {
      return rootFields;
    }

    public Set<String> getKnnFields() {
      return knnFields;
    }

    public boolean hasKnnFields() {
      return !knnFields.isEmpty();
    }

    protected void setRootCondition(Condition rootCondition) {
      if (rootCondition == null) {
        return;
      }
      this.rootCondition = rootCondition;
    }
  }


  boolean useSearchAfter(ElasticIndexConfig indexConfig, QueryParams params);
  QueryNodeObject build(ValidationData validationData, ElasticIndexConfig indexConfig, QueryParams params);
  QueryNodeObject build(ValidationData validationData, QueryParams params);
  QueryNodeObject buildAggregation(Aggregation aggregation);

}
