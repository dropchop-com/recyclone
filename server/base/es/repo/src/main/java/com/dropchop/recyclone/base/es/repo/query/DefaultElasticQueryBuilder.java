package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.query.*;
import com.dropchop.recyclone.base.api.model.query.aggregation.*;
import com.dropchop.recyclone.base.api.model.query.condition.*;
import com.dropchop.recyclone.base.api.model.query.operator.*;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.*;
import com.dropchop.recyclone.base.es.repo.config.ElasticIndexConfig;
import com.dropchop.recyclone.base.es.repo.config.HasDefaultSort;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;

@Slf4j
@ApplicationScoped
@SuppressWarnings({"IfCanBeSwitch", "unused"})
public class DefaultElasticQueryBuilder implements ElasticQueryBuilder {

  protected QueryNodeObject mapConditionField(int level, QueryFieldListener listener, Field<?> field,
                                              QueryNodeObject parentNodeObject) {
    OperatorNodeObject operatorNode = new OperatorNodeObject(parentNodeObject);
    String fieldName = field.getName();
    ConditionOperator operator;

    if (field instanceof ConditionedField conditionedField) {
      operator = conditionedField.get(fieldName);
      if (operator == null) {
        operator = new Eq<>(null);
      }
    } else {
      Object val = field.get(field.getName());
      operator = new Eq<>(val);
    }

    if (operator instanceof Eq<?> eq) {
      Object val = eq.get$eq();
      if (val == null) {
        operatorNode.addNullSearch(fieldName);
      } else {
        operatorNode.addEqOperator(fieldName, eq.get$eq());
      }
    } else if (operator instanceof Gt<?> gt) {
      operatorNode.addRangeOperator(fieldName, "gt", gt.get$gt());
    } else if (operator instanceof Lt<?> lt) {
      operatorNode.addRangeOperator(fieldName, "lt", lt.get$lt());
    } else if (operator instanceof Gte<?> gte) {
      operatorNode.addRangeOperator(fieldName, "gte", gte.get$gte());
    } else if (operator instanceof Lte<?> lte) {
      operatorNode.addRangeOperator(fieldName, "lte", lte.get$lte());
    } else if (operator instanceof In<?> in) {
      operatorNode.addInOperator(fieldName, in.get$in());
    } else if (operator instanceof ClosedInterval<?> interval) {
      operatorNode.addClosedInterval(fieldName, interval.get$gte(), interval.get$lte());
    } else if (operator instanceof OpenInterval<?> interval) {
      operatorNode.addOpenInterval(fieldName, interval.get$gte(), interval.get$lt());
    } else if (operator instanceof ClosedOpenInterval<?> interval) {
      operatorNode.addClosedOpenInterval(fieldName, interval.get$gte(), interval.get$lt());
    } else if (operator instanceof OpenClosedInterval<?> interval) {
      operatorNode.addOpenClosedInterval(fieldName, interval.get$gt(), interval.get$lte());
    } else if (operator instanceof Match<?> textMatch) {
      operatorNode.addTextSearch(fieldName, textMatch);
    } else {
      throw new ServiceException(
          ErrorCode.internal_error, "Unsupported query operator: [" + operator.getClass().getName() + "]"
      );
    }
    return operatorNode;
  }

  protected QueryNodeObject mapCondition(int level, QueryFieldListener listener,
                                         Condition condition, Condition parentCond,
                                         QueryNodeObject parentNodeObject) {
    if (condition == null) {
      return null;
    }

    // force bool.must on top level if not logical condition
    if (level == 0 && !(condition instanceof LogicalCondition)) {
      BoolQueryObject boolQuery = new BoolQueryObject();
      QueryNodeObject subQueryContainer = mapCondition(level + 1, listener, condition, null, boolQuery);
      boolQuery.must(subQueryContainer);
      return boolQuery;
    }

    if (condition instanceof LogicalCondition logicalCondition) {
      BoolQueryObject query = new BoolQueryObject(parentNodeObject);
      if (listener != null) {
        listener.on(0, condition, parentNodeObject);
      }

      for (Iterator<Condition> it = logicalCondition.iterator(); it.hasNext(); ) {
        Condition subCondition = it.next();
        QueryNodeObject subQueryContainer = mapCondition(level + 1, listener, subCondition, condition, query);
        if (condition instanceof And) {
          query.must(subQueryContainer);
        } else if (condition instanceof Or) {
          query.should(subQueryContainer);
        } else if (condition instanceof Not) {
          query.mustNot(subQueryContainer);
        }
      }
      return query;
    } else if (condition instanceof ConditionedField conditionedField) {
      QueryNodeObject mappedField = mapConditionField(level, listener, conditionedField, parentNodeObject);
      if (listener != null) {
        listener.on(level, conditionedField, mappedField);
      }
      return mappedField;
    } else if (condition instanceof Field<?> field) {
      QueryNodeObject mappedField = mapConditionField(level, listener, field, parentNodeObject);
      if (listener != null) {
        listener.on(level, field, mappedField);
      }
      return mappedField;
    } else if (condition instanceof Knn knnCondition) {
      QueryNodeObject filterQuery = null;
      KnnNodeObject knnNode = new KnnNodeObject(
          null, knnCondition, mapCondition(
              level + 1, listener, (knnCondition).get$knn().getFilter(), null, null
          )
      );
      if (listener != null) {
        listener.on(0, condition, knnNode);
      }

      return knnNode;
    }

    return parentNodeObject;
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
          List<String> includes = terms.getFilter().getInclude().getValue();
          if (includes != null && !includes.isEmpty()) {
            if (includes.size() == 1) {
              String include = includes.getFirst();
              if (include.contains("*")) { // detect if it is a regex -> don't pass it as an array
                termsNode.put("include", include);
              } else {
                termsNode.put("include", includes);
              }
            } else {
              termsNode.put("include", includes);
            }
          }
        }

        if (terms.getFilter().getExclude() != null) {
          List<String> excludes = terms.getFilter().getExclude().getValue();
          if (excludes != null && !excludes.isEmpty()) {
            if (excludes.size() == 1) {
              String exclude = excludes.getFirst();
              if (exclude.contains("*")) { // detect if it is a regex -> don't pass it as an array
                termsNode.put("exclude", exclude);
              } else {
                termsNode.put("exclude", excludes);
              }
            } else {
              termsNode.put("exclude", excludes);
            }
          }
        }
      }
      node.put("terms", termsNode);
    } else if (aggregation instanceof DateHistogram dh) {
      QueryNodeObject dhNode = new QueryNodeObject();
      dhNode.put("field", dh.getField());
      dhNode.put("calendar_interval", dh.getCalendar_interval());
      String tz = dh.getTime_zone();
      if (tz != null && !tz.isBlank()) {
        dhNode.put("time_zone", tz);
      }
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
    } else if (aggregation instanceof TopHits topHits) {
      TopHitsNodeObject topHitsNode = new TopHitsNodeObject();
      topHitsNode.setSize(topHits.getSize());
      for (Sort s : topHits.getSort()) {
        topHitsNode.addSort(s.getField(), s.getValue(), s.getNumericType());
      }
      if (topHits.getFilter() != null) {
        if (topHits.getFilter().getInclude() != null) {
          topHitsNode.setSourceIncludes(topHits.getFilter().getInclude());
        }
      }
      node.put("top_hits", topHitsNode);
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
    if (condition instanceof Knn) {
      return true;
    }
    if (condition instanceof LogicalCondition logicalCondition) {
      Iterator<Condition> iterator = logicalCondition.iterator();
      return iterator.hasNext();
    }
    return false;
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
            if (sort.startsWith("-")) {
              sortEntry.put(sort.substring(1), "desc");
            } else {
              if (sort.startsWith("+")) {
                sort = sort.substring(1);
              }
              sortEntry.put(sort, "asc");
            }
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
  public QueryNodeObject build(QueryFieldListener fieldListener, ElasticIndexConfig indexConfig, QueryParams params) {
    QueryNodeObject query = new QueryNodeObject();
    QueryNodeObject queryContainer = new QueryNodeObject();

    boolean useSearchAfterMode = useSearchAfter(indexConfig, params);
    QueryNodeObject sort = buildSortOrder(params.tryGetResultFilter().getSort(), indexConfig, useSearchAfterMode);

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

    Condition condition = params.getCondition();
    if (hasActualConditions(condition)) {
      QueryNodeObject boolQuery = mapCondition(0, fieldListener, condition, null, null);
      queryContainer.put("query", boolQuery);
    } else {
      queryContainer.put("query", new MatchAllObject());
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
    AggregationList aggsQuery = params.getAggregate();
    if (aggsQuery != null && !aggsQuery.isEmpty()) {
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


  public QueryNodeObject build(QueryFieldListener fieldListener, QueryParams params) {
    return build(fieldListener, null, params);
  }

  public QueryNodeObject build(QueryParams params) {
    return build(null, null, params);
  }
}
