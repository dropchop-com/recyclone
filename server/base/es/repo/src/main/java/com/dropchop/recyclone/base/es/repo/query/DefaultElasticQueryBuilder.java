package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.query.*;
import com.dropchop.recyclone.base.api.model.query.aggregation.*;
import com.dropchop.recyclone.base.api.model.query.condition.*;
import com.dropchop.recyclone.base.api.model.query.operator.*;
import com.dropchop.recyclone.base.api.model.query.operator.text.AdvancedText;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.dto.model.text.ExpressionToken;
import com.dropchop.recyclone.base.dto.model.text.Filter;
import com.dropchop.recyclone.base.dto.model.text.LegacyExpressionParser;
import com.dropchop.recyclone.base.es.model.query.*;
import com.dropchop.recyclone.base.es.repo.config.ElasticIndexConfig;
import com.dropchop.recyclone.base.es.repo.config.HasDefaultSort;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@ApplicationScoped
@SuppressWarnings({"IfCanBeSwitch", "unused"})
public class DefaultElasticQueryBuilder implements ElasticQueryBuilder {

  protected QueryNodeObject mapCondition(int level, QueryFieldListener listener,
                                         Condition condition, Condition parentCond,
                                         QueryNodeObject parentNodeObject) {
    if (condition instanceof LogicalCondition logicalCondition) {
      BoolQueryObject query = new BoolQueryObject();
      if (listener != null) {
        listener.on(0, null, condition, parentNodeObject);
      }

      for (Iterator<Condition> it = logicalCondition.iterator(); it.hasNext(); ) {
        Condition subCondition = it.next();
        BoolQueryObject queryContainer = new BoolQueryObject();
        if (condition instanceof And) {
          query.must(mapCondition(level + 1, listener, subCondition, condition, queryContainer));
        } else if (condition instanceof Or) {
          query.should(mapCondition(level + 1, listener, subCondition, condition, queryContainer));
        } else if (condition instanceof Not) {
          query.mustNot(mapCondition(level + 1, listener, subCondition, condition, queryContainer));
        }
      }
      return query;
    } else if (condition instanceof ConditionedField conditionedField) {
      String fieldName = conditionedField.getName();
      ConditionOperator operator = (ConditionOperator) conditionedField.values().toArray()[0];
      return mapConditionField(level, listener, fieldName, operator);
    } else if (condition instanceof Knn knnCondition) {
      KnnNodeObject knnNode = new KnnNodeObject(null, knnCondition);
      QueryNodeObject queryNodeObject = new QueryNodeObject();
      QueryNodeObject mustObject = new QueryNodeObject();

      if ((knnCondition).get$knn().getFilter() != null) {
        QueryNodeObject filterQuery = mapCondition(
            level + 1, listener, (knnCondition).get$knn().getFilter(), null, null);
        knnNode.addFilter(filterQuery);
      }
      if (listener != null) {
        listener.on(0, null, condition, knnNode);
      }
      queryNodeObject.put("knn", knnNode);

      if (parentCond != null) {
        return queryNodeObject;
      }

      mustObject.put("must", queryNodeObject);
      return mustObject;
    } else if (condition instanceof Field<?> field) {
      String fieldName = field.getName();

      if (field.iterator().next() instanceof ZonedDateTime) {
        OperatorNodeObject operator = new OperatorNodeObject();
        if (field.iterator().next() instanceof ZonedDateTime date) {
          operator.addClosedInterval(fieldName, date, date);
        }
        if (listener != null) {
          listener.on(0, fieldName, condition, operator);
        }
        return operator;
      }

      if (field.iterator().next() == null) {
        OperatorNodeObject operator = new OperatorNodeObject();
        operator.addNullSearch(fieldName);
        if (listener != null) {
          listener.on(0, fieldName, condition, operator);
        }
        return operator;
      }

      QueryNodeObject mustWrapper = new QueryNodeObject();
      QueryNodeObject query = new QueryNodeObject();
      QueryNodeObject termWrapper = new QueryNodeObject();
      query.put(fieldName, field.iterator().next());
      termWrapper.put("term", query);
      mustWrapper.put("must", termWrapper);

      if (parentNodeObject == null) {
        if (listener != null) {
          listener.on(0, fieldName, condition, mustWrapper);
        }
        return mustWrapper;
      }

      if (listener != null) {
        listener.on(0, fieldName, condition, termWrapper);
      }
      return termWrapper;
    }

    return parentNodeObject;
  }

  protected QueryNodeObject mapTextCondition(Match<?> textMatch, BoolQueryObject previousCondition) {
    return previousCondition;
  }

  private QueryNodeObject expressionToQueryNode(String defaultField, ExpressionToken token) {
    if (token instanceof com.dropchop.recyclone.base.dto.model.text.Or orToken) {
      BoolQueryObject orBool = new BoolQueryObject();
      for (ExpressionToken part : orToken.getExpressionTokens()) {
        QueryNodeObject child = expressionToQueryNode(defaultField, part);
        orBool.should(child);
      }
      orBool.setMinimumShouldMatch(1);
      return orBool;
    }

    String field = defaultField;
    if (token instanceof Filter filter && filter.getName() != null && !filter.getName().isEmpty()) {
      field = filter.getName();
    }

    if (token instanceof com.dropchop.recyclone.base.dto.model.text.Phrase phrase) {
      String phraseText = phrase.getExpression().toString();
      QueryNodeObject matchPhrase = new QueryNodeObject();
      QueryNodeObject conf = new QueryNodeObject();
      conf.put("query", phraseText);
      Map<String, Object> meta = phrase.getMetaData();
      if (meta != null) {
        copyIfPresent(meta, conf, "slop");
        copyIfPresent(meta, conf, "boost");
        copyIfPresent(meta, conf, "analyzer");
      }
      QueryNodeObject wrapper = new QueryNodeObject();
      wrapper.put(field, conf);
      matchPhrase.put("match_phrase", wrapper);
      return matchPhrase;
    }
    String term = token.getExpression().toString();
    if (token.isPrefix() || term.indexOf('*') >= 0) {
      QueryNodeObject wildcard = new QueryNodeObject();
      QueryNodeObject conf = new QueryNodeObject();
      conf.put("value", term);
      conf.put("case_insensitive", true);
      Map<String, Object> meta = token.getMetaData();
      if (meta != null) {
        copyIfPresent(meta, conf, "boost");
      }
      QueryNodeObject wrapper = new QueryNodeObject();
      wrapper.put(field, conf);
      wildcard.put("wildcard", wrapper);
      return wildcard;
    } else {
      QueryNodeObject match = new QueryNodeObject();
      QueryNodeObject conf = new QueryNodeObject();
      conf.put("query", term);
      Map<String, Object> meta = token.getMetaData();
      if (meta != null) {
        copyIfPresent(meta, conf, "fuzziness");
        copyIfPresent(meta, conf, "boost");
        copyIfPresent(meta, conf, "analyzer");
        copyIfPresent(meta, conf, "operator");
      }
      QueryNodeObject wrapper = new QueryNodeObject();
      wrapper.put(field, conf);
      match.put("match", wrapper);
      return match;
    }
  }

  private QueryNodeObject mapAdvancedText(String defaultField, String value) {
    List<ExpressionToken> tokens = LegacyExpressionParser.parse(value, false, true, true);

    BoolQueryObject inner = new BoolQueryObject();
    for (ExpressionToken token : tokens) {
      QueryNodeObject node = expressionToQueryNode(defaultField, token);
      if (token.isMustNot()) {
        inner.mustNot(node);
      } else if (token.isMust()) {
        inner.must(node);
      } else {
        inner.should(node);
      }
    }
    if (!inner.containsKey("must") && !inner.containsKey("must_not") && inner.getShould() != null) {
      inner.setMinimumShouldMatch(1);
    }

    QueryNodeObject wrapper = new QueryNodeObject();
    wrapper.put("bool", inner);
    return wrapper;
  }


  private void copyIfPresent(Map<String, Object> meta, QueryNodeObject to, String key) {
    Object v = meta.get(key);
    if (v != null) to.put(key, v);
  }

  protected OperatorNodeObject mapConditionField(int level, QueryFieldListener listener, String field,
                                                 ConditionOperator operator) {
    OperatorNodeObject operatorNode = new OperatorNodeObject();

    if (operator instanceof Eq) {
      operatorNode.addEqOperator(field, ((Eq<?>) operator).get$eq());
    } else if (operator instanceof Gt<?> gt) {
      operatorNode.addRangeOperator(field, "gt", gt.get$gt());
    } else if (operator instanceof Lt<?> lt) {
      operatorNode.addRangeOperator(field, "lt", lt.get$lt());
    } else if (operator instanceof Gte<?> gte) {
      operatorNode.addRangeOperator(field, "gte", gte.get$gte());
    } else if (operator instanceof Lte<?> lte) {
      operatorNode.addRangeOperator(field, "lte", lte.get$lte());
    } else if (operator instanceof In<?> in) {
      operatorNode.addInOperator(field, in.get$in());
    } else if (operator instanceof ClosedInterval<?> interval) {
      operatorNode.addClosedInterval(field, interval.get$gte(), interval.get$lte());
    } else if (operator instanceof OpenInterval<?> interval) {
      operatorNode.addOpenInterval(field, interval.get$gte(), interval.get$lt());
    } else if (operator instanceof ClosedOpenInterval<?> interval) {
      operatorNode.addClosedOpenInterval(field, interval.get$gte(), interval.get$lt());
    } else if (operator instanceof OpenClosedInterval<?> interval) {
      operatorNode.addOpenClosedInterval(field, interval.get$gt(), interval.get$lte());
    } else if (operator instanceof Match<?> textMatch) {
      Object raw = textMatch.get$match();
      if (raw instanceof AdvancedText text) {
        QueryNodeObject advancedQuery = mapAdvancedText(field, text.getValue());
        if (!advancedQuery.isEmpty()) {
          OperatorNodeObject wrapper = new OperatorNodeObject();
          wrapper.putAll(advancedQuery);
          if (listener != null) {
            listener.on(level, field, operator, wrapper);
          }
          return wrapper;
        }
      }
      operatorNode.addTextSearch(field, textMatch);
    } else if (operator == null) {
      operatorNode.addNullSearch(field);
    } else {
      operatorNode.addEqOperator(field, operator);
    }

    if (listener != null) {
      listener.on(level, field, operator, operatorNode);
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
        topHitsNode.addSort(s.getField(), s.getValue());
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

    if (condition instanceof LogicalCondition logicalCondition) {
      Iterator<Condition> iterator = logicalCondition.iterator();
      return iterator.hasNext();
    }

    return true;
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
    boolean makeBoolQuery = condition instanceof LogicalCondition logicalCondition
        && logicalCondition.iterator().hasNext();
    if (!makeBoolQuery) {
      makeBoolQuery = condition instanceof Knn;
    }
    if (makeBoolQuery) {
      QueryNodeObject conditions = mapCondition(
          0, fieldListener, condition, null, null
      );
      query.put("bool", conditions);
      queryContainer.put("query", query);
    } else {
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
