package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.query.*;
import com.dropchop.recyclone.base.api.model.query.aggregation.*;
import com.dropchop.recyclone.base.api.model.query.condition.And;
import com.dropchop.recyclone.base.api.model.query.condition.LogicalCondition;
import com.dropchop.recyclone.base.api.model.query.condition.Not;
import com.dropchop.recyclone.base.api.model.query.condition.Or;
import com.dropchop.recyclone.base.api.model.query.knn.KnnQuery;
import com.dropchop.recyclone.base.api.model.query.operator.*;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.BoolQueryObject;
import com.dropchop.recyclone.base.es.model.query.MatchAllObject;
import com.dropchop.recyclone.base.es.model.query.OperatorNodeObject;
import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;
import com.dropchop.recyclone.base.es.repo.config.ElasticIndexConfig;
import com.dropchop.recyclone.base.es.repo.config.HasDefaultSort;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@ApplicationScoped
@SuppressWarnings({"IfCanBeSwitch", "unused"})
public class DefaultElasticQueryBuilder implements ElasticQueryBuilder {

  protected QueryNodeObject mapCondition(ValidationData validationData, Condition condition, Condition parentCond,
                                         QueryNodeObject parentNodeObject) {
    if (condition instanceof LogicalCondition logicalCondition) {
      BoolQueryObject query = new BoolQueryObject();
      if ((condition instanceof And || condition instanceof Or) && validationData != null) {
        validationData.setRootCondition(condition);
      }

      for (Iterator<Condition> it = logicalCondition.iterator(); it.hasNext(); ) {
        Condition subCondition = it.next();
        BoolQueryObject queryContainer = new BoolQueryObject();
        if (condition instanceof And) {
          query.must(mapCondition(validationData, subCondition, condition, queryContainer));
        } else if (condition instanceof Or) {
          query.should(mapCondition(validationData, subCondition, condition, queryContainer));
        } else if (condition instanceof Not) {
          query.mustNot(mapCondition(validationData, subCondition, condition, queryContainer));
        }
      }
      return query;
    } else if (condition instanceof ConditionedField conditionedField) {
      String fieldName = conditionedField.getName();
      ConditionOperator operator = (ConditionOperator) conditionedField.values().toArray()[0];
      if (validationData != null) {
        validationData.addRootField(fieldName, parentCond);
      }
      return mapConditionField(fieldName, operator);
    } else if (condition instanceof Field<?> field) {
      String fieldName = field.getName();
      if (validationData != null) {
        validationData.addRootField(fieldName, parentCond);
      }
      if (field.iterator().next() instanceof ZonedDateTime) {
        OperatorNodeObject operator = new OperatorNodeObject();
        if (field.iterator().next() instanceof ZonedDateTime date) {
          operator.addClosedInterval(fieldName, date, date);
        }
        return operator;
      }

      if (field.iterator().next() == null) {
        OperatorNodeObject operator = new OperatorNodeObject();
        operator.addNullSearch(fieldName);
        return operator;
      }

      QueryNodeObject mustWrapper = new QueryNodeObject();
      QueryNodeObject query = new QueryNodeObject();
      QueryNodeObject termWrapper = new QueryNodeObject();
      query.put(fieldName, field.iterator().next());
      termWrapper.put("term", query);
      mustWrapper.put("must", termWrapper);

      if (parentNodeObject == null) {
        return mustWrapper;
      }

      return termWrapper;
    }

    return parentNodeObject;
  }

  protected QueryNodeObject mapTextCondition(Match<?> textMatch, BoolQueryObject previousCondition) {
    return previousCondition;
  }

  protected OperatorNodeObject mapConditionField(String field, ConditionOperator operator) {
    OperatorNodeObject operatorNode = new OperatorNodeObject();

    if (operator instanceof Eq) {
      operatorNode.addEqOperator(field, ((Eq<?>) operator).get$eq());
    } else if (operator instanceof Gt) {
      operatorNode.addRangeOperator(field, "gt", ((Gt<?>) operator).get$gt());
    } else if (operator instanceof Lt) {
      operatorNode.addRangeOperator(field, "lt", ((Lt<?>) operator).get$lt());
    } else if (operator instanceof Gte) {
      operatorNode.addRangeOperator(field, "gte", ((Gte<?>) operator).get$gte());
    } else if (operator instanceof Lte) {
      operatorNode.addRangeOperator(field, "lte", ((Lte<?>) operator).get$lte());
    } else if (operator instanceof In) {
      operatorNode.addInOperator(field, ((In<?>) operator).get$in());
    } else if (operator instanceof ClosedInterval<?> interval) {
      operatorNode.addClosedInterval(field, interval.get$gte(), interval.get$lte());
    } else if (operator instanceof OpenInterval<?> interval) {
      operatorNode.addOpenInterval(field, interval.get$gte(), interval.get$lt());
    } else if (operator instanceof ClosedOpenInterval<?> interval) {
      operatorNode.addClosedOpenInterval(field, interval.get$gte(), interval.get$lt());
    } else if (operator instanceof OpenClosedInterval<?> interval) {
      operatorNode.addOpenClosedInterval(field, interval.get$gt(), interval.get$lte());
    } else if (operator instanceof Match<?> textMatch) {
      operatorNode.addTextSearch(field, textMatch);
    } else if (operator == null) {
      operatorNode.addNullSearch(field);
    } else {
      operatorNode.addEqOperator(field, operator);
    }

    return operatorNode;
  }

  @Override
  public QueryNodeObject buildAggregation(Aggregation aggregation) {
    QueryNodeObject node = new QueryNodeObject();

    if (aggregation instanceof Terms terms) {
      QueryNodeObject termsNode = new QueryNodeObject();
      termsNode.put("field", terms.getField());

      if (terms.getSize() != null) {
        termsNode.put("size", terms.getSize());
      }

      if (terms.getFilter() != null) {
        if (terms.getFilter().getInclude() != null) {
          termsNode.put("include", terms.getFilter().getInclude().getValue());
        }

        if (terms.getFilter().getExclude() != null) {
          termsNode.put("exclude", terms.getFilter().getExclude().getValue());
        }
      }
      node.put("terms", termsNode);
    } else if (aggregation instanceof DateHistogram dh) {
      QueryNodeObject dhNode = new QueryNodeObject();
      dhNode.put("field", dh.getField());
      dhNode.put("calendar_interval", dh.getCalendar_interval());

      node.put("date_histogram", dhNode);
    } else if (aggregation instanceof Avg) {
      QueryNodeObject avg = new QueryNodeObject();
      avg.put("field", aggregation.getField());
      node.put("avg", avg);
    } else if (aggregation instanceof Count) {
      QueryNodeObject count = new QueryNodeObject();
      count.put("field", aggregation.getField());
      node.put("value_count", count);
    } else if (aggregation instanceof Max) {
      QueryNodeObject max = new QueryNodeObject();
      max.put("field", aggregation.getField());
      node.put("max", max);
    } else if (aggregation instanceof Min) {
      QueryNodeObject min = new QueryNodeObject();
      min.put("field", aggregation.getField());
      node.put("min", min);
    } else if (aggregation instanceof Sum) {
      QueryNodeObject sum = new QueryNodeObject();
      sum.put("field", aggregation.getField());
      node.put("sum", sum);
    } else if (aggregation instanceof Cardinality) {
      QueryNodeObject cardinality = new QueryNodeObject();
      cardinality.put("field", aggregation.getField());
      node.put("cardinality", cardinality);
    } else if (aggregation instanceof Stats) {
      QueryNodeObject stats = new QueryNodeObject();
      stats.put("field", aggregation.getField());
      node.put("stats", stats);
    }

    if (aggregation instanceof BucketAggregation bucket) {
      if (bucket.getAggs() != null && !bucket.getAggs().isEmpty()) {
        QueryNodeObject subAggs = new QueryNodeObject();
        for (Aggregation sub : bucket.getAggs()) {
          if (sub instanceof Aggregation.Wrapper) {
            subAggs.put(sub.getName(), buildAggregation(((Aggregation.Wrapper) sub).iterator().next()));
          } else {
            subAggs.put(sub.getName(), buildAggregation(sub));
          }
        }
        node.put("aggs", subAggs);
      }
    }

    return node;
  }

  protected boolean hasActualConditions(Condition condition) {
    if (condition == null) {
      return false;
    }

    if (condition instanceof LogicalCondition logicalCondition) {
      Iterator<Condition> iterator = logicalCondition.iterator();
      return iterator.hasNext();
    }

    return true;
  }

  protected boolean isSimpleKnnSearch(QueryParams params) {
    boolean hasConditions = params.getCondition() != null && hasActualConditions(params.getCondition());
    return !hasConditions && params.getKnnQuery() != null;
  }

  protected void ensureBoolQueryStructure(QueryNodeObject query) {
    if (!query.containsKey("bool")) {
      Object existingQuery = null;
      if (query.containsKey("match_all")) {
        existingQuery = query.get("match_all");
        query.remove("match_all");
      }

      BoolQueryObject boolQuery = new BoolQueryObject();
      if (existingQuery != null) {
        QueryNodeObject matchAllWrapper = new QueryNodeObject();
        matchAllWrapper.put("match_all", existingQuery);
        boolQuery.must(matchAllWrapper);
      }

      query.put("bool", boolQuery);
    }
  }

  protected QueryNodeObject buildKnnQuery(KnnQuery knnQuery, ValidationData validationData) {
    QueryNodeObject knnNode = new QueryNodeObject();

    knnNode.put("field", knnQuery.getField());

    if (validationData != null) {
      validationData.addKnnField(knnQuery.getField());
    }

    if (knnQuery.getQueryVector() != null) {
      knnNode.put("query_vector", knnQuery.getQueryVector());
    } else {
      throw new ServiceException(
        ErrorCode.parameter_validation_error,
        "kNN query must have query_vector"
      );
    }

    if (knnQuery.getK() != null) {
      knnNode.put("k", knnQuery.getK());
    }

    if (knnQuery.getNumCandidates() != null) {
      if (knnQuery.getNumCandidates() > 10000) {
        throw new ServiceException(
          ErrorCode.parameter_validation_error,
          "num_candidates cannot exceed 10,000"
        );
      }
      knnNode.put("num_candidates", knnQuery.getNumCandidates());
    }

    if (knnQuery.getFilter() != null) {
      ValidationData filterValidation = new ValidationData();
      QueryNodeObject filterQuery = mapCondition(filterValidation, knnQuery.getFilter(),
        null, null);

      QueryNodeObject wrappedFilter = new QueryNodeObject();
      wrappedFilter.put("bool", filterQuery);
      knnNode.put("filter", wrappedFilter);
    }

    if (knnQuery.getSimilarity() != null) {
      knnNode.put("similarity", knnQuery.getSimilarity());
    }

    if (knnQuery.getBoost() != null && !knnQuery.getBoost().equals(1.0f)) {
      knnNode.put("boost", knnQuery.getBoost());
    }

    if (knnQuery.getName() != null) {
      knnNode.put("_name", knnQuery.getName());
    }

    return knnNode;
  }

  @Override
  public boolean useSearchAfter(ElasticIndexConfig indexConfig, QueryParams queryParams) {
    if (indexConfig == null) {
      return false;
    }
    int requestSize = queryParams.tryGetResultFilter().getSize();
    int requestFrom = queryParams.tryGetResultFilter().getFrom();
    int maxSize = indexConfig.getSizeOfPagination();
    return requestSize + requestFrom >= maxSize;  // we would overflow allowed elastic maximum
  }

  protected QueryNodeObject buildSortOrder(List<String> sortList, ElasticIndexConfig indexConfig,
                                           boolean useSearchAfter) {
    QueryNodeObject sortOrder = new QueryNodeObject();
    if (!sortList.isEmpty()) {
      List<QueryNodeObject> sortEntries = sortList.stream()
          .map(sort -> {
            QueryNodeObject sortEntry = new QueryNodeObject();
            sortEntry.put(sort, "desc");
            return sortEntry;
          })
          .toList();
      sortOrder.put("sort", sortEntries);
      return sortOrder;
    } else if (indexConfig instanceof HasDefaultSort hasDefaultSort) {
      QueryNodeObject defaultSort = hasDefaultSort.getSortOrder();
      if ((defaultSort == null || defaultSort.isEmpty()) && useSearchAfter) {
        throw new ServiceException(
            ErrorCode.internal_error, "No sort order received from index config for deep pagination!"
        );
      }
      if (defaultSort != null && !defaultSort.isEmpty()) {
        List<QueryNodeObject> sortEntries = defaultSort.entrySet().stream().map(
            e -> {
              QueryNodeObject sortEntry = new QueryNodeObject();
              sortEntry.put(e.getKey(), e.getValue());
              return sortEntry;
            }
        ).toList();
        sortOrder.put("sort", sortEntries);
        return sortOrder;
      }
    }
    return null;
  }

  @Override
  public QueryNodeObject build(ValidationData validationData, ElasticIndexConfig indexConfig, QueryParams params) {
    QueryNodeObject queryContainer = new QueryNodeObject();

    boolean useSearchAfterMode = useSearchAfter(indexConfig, params);
    QueryNodeObject sort = buildSortOrder(params.tryGetResultFilter().sort(), indexConfig, useSearchAfterMode);

    int size = params.tryGetResultFilter().size();
    int from = params.tryGetResultFilter().from();

    if (useSearchAfterMode) {
      if (sort == null) {
        throw new ServiceException(
            ErrorCode.parameter_validation_error, "Sort must be defined when using search-after mode!"
        );
      }
      queryContainer.put("size", size);
    } else {
      queryContainer.put("from", from);
      queryContainer.put("size", size);
    }

    if (sort != null) {
      queryContainer.putAll(sort);
    }

    boolean hasKnnQuery = params.getKnnQuery() != null;
    boolean hasConditions = params.getCondition() != null && hasActualConditions(params.getCondition());

    if (hasConditions || hasKnnQuery) {
      if (hasKnnQuery && !hasConditions) {
        QueryNodeObject knnQuery = buildKnnQuery(params.getKnnQuery(), validationData);
        queryContainer.put("knn", knnQuery);

        QueryNodeObject query = new QueryNodeObject();
        MatchAllObject matchAll = new MatchAllObject();
        query.put("match_all", matchAll);
        queryContainer.put("query", query);
      } else if (!hasKnnQuery) {
        QueryNodeObject conditions = mapCondition(
          validationData, params.getCondition(), null, null
        );
        QueryNodeObject query = new QueryNodeObject();
        query.put("bool", conditions);
        queryContainer.put("query", query);
      } else {
        QueryNodeObject conditions = mapCondition(
          validationData, params.getCondition(), null, null
        );

        QueryNodeObject hybridQuery = new QueryNodeObject();
        QueryNodeObject boolSection = new QueryNodeObject();

        List<Object> mustArray = new ArrayList<>();
        if (conditions instanceof BoolQueryObject) {
          QueryNodeObject conditionsWrapper = new QueryNodeObject();
          conditionsWrapper.put("bool", conditions);
          mustArray.add(conditionsWrapper);
        } else {
          mustArray.add(conditions);
        }
        boolSection.put("must", mustArray);

        List<Object> shouldArray = new ArrayList<>();
        QueryNodeObject knnWrapper = new QueryNodeObject();
        knnWrapper.put("knn", buildKnnQuery(params.getKnnQuery(), validationData));
        shouldArray.add(knnWrapper);
        boolSection.put("should", shouldArray);

        hybridQuery.put("bool", boolSection);
        queryContainer.put("query", hybridQuery);
      }
    } else {
      QueryNodeObject query = new QueryNodeObject();
      MatchAllObject matchAll = new MatchAllObject();
      query.put("match_all", matchAll);
      queryContainer.put("query", query);
    }

    ResultFilter.ContentFilter filter = params.tryGetResultContentFilter();
    if (filter != null) {
      QueryNodeObject source = new QueryNodeObject();
      List<String> excludes = filter.getExcludes();
      if (excludes != null && !excludes.isEmpty()) {
        source.put("excludes", excludes);
      }
      List<String> includes = filter.getIncludes();
      if (includes != null && !includes.isEmpty()) {
        source.put("includes", includes);
      }
      if (!source.isEmpty()) {
        queryContainer.put("_source", source);
      }
    }

    // Handle aggregations
    if (params.getAggregate() != null) {
      QueryNodeObject aggregations = new QueryNodeObject();
      for (Aggregation agg : params.getAggregate()) {
        if (agg instanceof Aggregation.Wrapper) {
          aggregations.put(agg.getName(), buildAggregation(((Aggregation.Wrapper) agg).iterator().next()));
        } else {
          aggregations.put(agg.getName(), buildAggregation(agg));
        }
      }
      queryContainer.put("aggs", aggregations);
    }

    return queryContainer;
  }


  public QueryNodeObject build(ValidationData validationData, QueryParams params) {
    return build(validationData, null, params);
  }
}
