package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.query.*;
import com.dropchop.recyclone.base.api.model.query.aggregation.*;
import com.dropchop.recyclone.base.api.model.query.aggregation.Sort;
import com.dropchop.recyclone.base.api.model.query.aggregation.Terms;
import com.dropchop.recyclone.base.api.model.query.condition.*;
import com.dropchop.recyclone.base.api.model.query.operator.*;
import com.dropchop.recyclone.base.api.model.query.operator.Match;
import com.dropchop.recyclone.base.api.model.query.operator.text.AdvancedText;
import com.dropchop.recyclone.base.api.model.query.operator.text.Phrase;
import com.dropchop.recyclone.base.api.model.query.operator.text.Text;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.*;
import com.dropchop.recyclone.base.es.model.query.cond.*;
import com.dropchop.recyclone.base.es.model.query.cond.Knn;
import com.dropchop.recyclone.base.es.model.query.cond.TopHits;
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

  protected IQueryObject mapField(int level, QueryFieldListener listener,
                                  Field<?> field,
                                  ConditionOperator operator,
                                  IQueryObject parentNodeObject) {
    IQueryObject fieldNode;
    String fieldName = field.getName();
    if (operator instanceof Eq<?> eq) {
      Object val = eq.get$eq();
      if (val == null) {
        fieldNode = new Exists(parentNodeObject, fieldName);
      } else {
        fieldNode = new Term(parentNodeObject, fieldName, eq.get$eq());
      }
    } else if (operator instanceof Gt<?> gt) {
      fieldNode = new Range(parentNodeObject, fieldName, gt);
    } else if (operator instanceof Lt<?> lt) {
      fieldNode = new Range(parentNodeObject, fieldName, lt);
    } else if (operator instanceof Gte<?> gte) {
      fieldNode = new Range(parentNodeObject, fieldName, gte);
    } else if (operator instanceof Lte<?> lte) {
      fieldNode = new Range(parentNodeObject, fieldName, lte);
    } else if (operator instanceof In<?> in) {
      fieldNode = new com.dropchop.recyclone.base.es.model.query.cond.Terms(
          parentNodeObject, fieldName, in.get$in()
      );
    } else if (operator instanceof ClosedInterval<?> interval) {
      fieldNode = new Range(parentNodeObject, fieldName, interval);
    } else if (operator instanceof OpenInterval<?> interval) {
      fieldNode = new Range(parentNodeObject, fieldName, interval);
    } else if (operator instanceof ClosedOpenInterval<?> interval) {
      fieldNode = new Range(parentNodeObject, fieldName, interval);
    } else if (operator instanceof OpenClosedInterval<?> interval) {
      fieldNode = new Range(parentNodeObject, fieldName, interval);
    } else if (operator instanceof Match<?> textMatch) {
      Text text = textMatch.getText();
      //noinspection IfCanBeSwitch
      if (text instanceof com.dropchop.recyclone.base.api.model.query.operator.text.Wildcard wildcard) {
        fieldNode = new com.dropchop.recyclone.base.es.model.query.cond.Wildcard(
            parentNodeObject, fieldName, text.getValue(), wildcard.getCaseInsensitive(), wildcard.getBoost()
        );
      } else if (text instanceof Phrase phrase) {
        fieldNode = new MatchPhrase(
            parentNodeObject, fieldName, text.getValue(), phrase.getSlop(), phrase.getAnalyzer()
        );
      } else if(text instanceof AdvancedText advancedText) {
        fieldNode = new com.dropchop.recyclone.base.es.model.query.cond.MatchText(
            parentNodeObject, fieldName, advancedText
        );
      } else {
        throw new IllegalArgumentException("Unsupported match text type: " + text.getClass().getName());
      }
    } else {
      throw new ServiceException(
          ErrorCode.internal_error, "Unsupported query operator: [" + operator.getClass().getName() + "]"
      );
    }
    return fieldNode;
  }

  private boolean mustHandleNull(ConditionOperator operator) {
    if (operator == null) {
      return true;
    }
    if (operator instanceof Eq<?> eq) {
      return eq.get$eq() == null;
    }
    return false;
  }

  private IQueryObject mapFieldCondition(int level, QueryFieldListener listener, Field<?> field,
                                         ConditionOperator operator, IQueryObject parentNodeObject) {
    IQueryObject mappedField = mapField(level, listener, field, operator, parentNodeObject);
    if (listener != null) {
      listener.on(level, field, mappedField);
    }
    if (mappedField instanceof Exists exists && operator instanceof Eq<?> eq && eq.get$eq() == null) {
      if (parentNodeObject instanceof Bool bool) {
        bool.mustNot(mappedField);
        return null; // this field was mapped here instead upon return from mapCondition (we reuse bool parent)
      } else {
        Bool boolQuery = new Bool(parentNodeObject);
        boolQuery.mustNot(mappedField);
        return boolQuery;
      }
    }
    return mappedField;
  }

  protected IQueryObject mapCondition(int level, QueryFieldListener listener, Condition condition,
                                      Condition parentCond, IQueryObject parentNodeObject) {
    if (condition == null) {
      return null;
    }

    // force bool.must on top level if not logical condition
    if (level == 0 && !(condition instanceof LogicalCondition)) {
      Bool boolQuery = new Bool();
      IQueryObject subQueryContainer = mapCondition(level + 1, listener, condition, null, boolQuery);
      if (subQueryContainer == null) { // null is returned when callee already handled this condition (Not Exists)
        return boolQuery;
      }
      boolQuery.must(subQueryContainer);
      return boolQuery;
    }

    if (condition instanceof LogicalCondition logicalCondition) {
      Bool query = new Bool(parentNodeObject);
      if (listener != null) {
        listener.on(0, condition, parentNodeObject);
      }

      for (Iterator<Condition> it = logicalCondition.iterator(); it.hasNext(); ) {
        Condition subCondition = it.next();
        IQueryObject subQueryContainer = mapCondition(level + 1, listener, subCondition, condition, query);
        if (subQueryContainer == null) { // null is returned when callee already handled this condition (Not Exists)
          continue;
        }
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
      ConditionOperator operator = conditionedField.get(conditionedField.getName());
      if (operator == null) {
        operator = new Eq<>(null);
      }
      return mapFieldCondition(level, listener, conditionedField, operator, parentNodeObject);
    } else if (condition instanceof Field<?> field) {
      Object val = field.get(field.getName());
      ConditionOperator operator = new Eq<>(val); // implicit synthetic Eq operator when Field type
      return mapFieldCondition(level, listener, field, operator, parentNodeObject);
    } else if (condition instanceof com.dropchop.recyclone.base.api.model.query.condition.Knn knnCondition) {
      IQueryObject filterQuery = null;
      Knn knnNode = new Knn(
          null, knnCondition, mapCondition(
              level + 1, listener, (knnCondition).get$knn().getFilter(), null, null
          )
      );
      if (listener != null) {
        listener.on(level, condition, knnNode);
      }

      return knnNode;
    }

    return parentNodeObject;
  }

  public QueryObject buildAggregationNew(QueryFieldListener listener, Aggregation aggregation) {
    if (aggregation == null) {
      return null;
    }

    if (aggregation instanceof Aggregation.Wrapper) {
      aggregation = ((Aggregation.Wrapper) aggregation).iterator().next();
    }

    QueryObject node;

    if (aggregation instanceof Terms terms) {
      com.dropchop.recyclone.base.es.model.query.agg.Terms termsNode =
          new com.dropchop.recyclone.base.es.model.query.agg.Terms(terms.getField());
      termsNode.setSize(terms.getSize());
      termsNode.setFilter(terms.getFilter());
      node = termsNode;
    } else if (aggregation instanceof DateHistogram dh) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.DateHistogram(
          dh.getField(), dh.getCalendar_interval(), dh.getTime_zone()
      );
    } else if (aggregation instanceof Avg) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Avg(aggregation.getField());
    } else if (aggregation instanceof Count) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Count(aggregation.getField());
    } else if (aggregation instanceof Max) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Max(aggregation.getField());
    } else if (aggregation instanceof Min) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Min(aggregation.getField());
    } else if (aggregation instanceof Sum) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Sum(aggregation.getField());
    } else if (aggregation instanceof Cardinality) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Cardinality(aggregation.getField());
    } else if (aggregation instanceof Stats) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Stats(aggregation.getField());
    } else if (aggregation instanceof com.dropchop.recyclone.base.api.model.query.aggregation.TopHits topHits) {
      com.dropchop.recyclone.base.es.model.query.agg.TopHits topHitsNode =
          new com.dropchop.recyclone.base.es.model.query.agg.TopHits();
      topHitsNode.setSize(topHits.getSize());
      if (topHits.getSort() != null) {
        for (Sort s : topHits.getSort()) {
          topHitsNode.addSort(s.getField(), s.getValue(), s.getNumericType());
        }
      }
      if (topHits.getFilter() != null && topHits.getFilter().getInclude() != null) {
        topHitsNode.setSourceIncludes(topHits.getFilter().getInclude());
      }
      node = topHitsNode;
    } else {
      node = new QueryObject();
    }

    if (aggregation instanceof BucketAggregation bucket) {
      if (bucket.getAggs() != null && !bucket.getAggs().isEmpty()
          && node instanceof com.dropchop.recyclone.base.es.model.query.agg.AggregationBucket bucketNode) {
        for (Aggregation sub : bucket.getAggs()) {
          if (sub instanceof Aggregation.Wrapper) {
            sub = ((Aggregation.Wrapper) sub).iterator().next();
          }
          IQueryObject subAggObj = buildAggregation(listener, sub);
          bucketNode.addAgg(sub.getName(), subAggObj);
        }
      }
    }

    return node;
  }

  public QueryObject buildAggregation(QueryFieldListener listener, Aggregation aggregation) {
    QueryObject node = new QueryObject();

    if (aggregation instanceof Terms terms) {
      QueryObject termsNode = new QueryObject();
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
      QueryObject dhNode = new QueryObject();
      dhNode.put("field", dh.getField());
      dhNode.put("calendar_interval", dh.getCalendar_interval());
      String tz = dh.getTime_zone();
      if (tz != null && !tz.isBlank()) {
        dhNode.put("time_zone", tz);
      }
      node.put("date_histogram", dhNode);
    } else if (aggregation instanceof Avg) {
      QueryObject avg = new QueryObject();
      avg.put("field", aggregation.getField());
      node.put("avg", avg);
    } else if (aggregation instanceof Count) {
      QueryObject count = new QueryObject();
      count.put("field", aggregation.getField());
      node.put("value_count", count);
    } else if (aggregation instanceof Max) {
      QueryObject max = new QueryObject();
      max.put("field", aggregation.getField());
      node.put("max", max);
    } else if (aggregation instanceof Min) {
      QueryObject min = new QueryObject();
      min.put("field", aggregation.getField());
      node.put("min", min);
    } else if (aggregation instanceof Sum) {
      QueryObject sum = new QueryObject();
      sum.put("field", aggregation.getField());
      node.put("sum", sum);
    } else if (aggregation instanceof Cardinality) {
      QueryObject cardinality = new QueryObject();
      cardinality.put("field", aggregation.getField());
      node.put("cardinality", cardinality);
    } else if (aggregation instanceof Stats) {
      QueryObject stats = new QueryObject();
      stats.put("field", aggregation.getField());
      node.put("stats", stats);
    } else if (aggregation instanceof com.dropchop.recyclone.base.api.model.query.aggregation.TopHits topHits) {
      TopHits topHitsNode = new TopHits();
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
        QueryObject subAggs = new QueryObject();
        for (Aggregation sub : bucket.getAggs()) {
          if (sub instanceof Aggregation.Wrapper) {
            // unwrap Aggregation
            sub = ((Aggregation.Wrapper) sub).iterator().next();
          }
          IQueryObject subAggObj = buildAggregation(listener, sub);
          subAggs.put(sub.getName(), subAggObj);
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
    if (condition instanceof com.dropchop.recyclone.base.api.model.query.condition.Knn) {
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

  protected IQueryObject buildSortOrder(List<String> sortList, ElasticIndexConfig indexConfig, boolean useSearchAfter) {
    IQueryObject sortOrder = new QueryObject();
    if (!sortList.isEmpty()) {
      List<IQueryObject> sortEntries = sortList.stream()
          .map(sort -> {
            IQueryObject sortEntry = new QueryObject();
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
      IQueryObject defaultSort = hasDefaultSort.getSortOrder();
      if ((defaultSort == null || defaultSort.isEmpty()) && useSearchAfter) {
        throw new ServiceException(
            ErrorCode.internal_error, "No sort order received from index config for deep pagination!"
        );
      }
      if (defaultSort != null && !defaultSort.isEmpty()) {
        List<QueryObject> sortEntries = defaultSort.entrySet().stream().map(
            e -> {
              QueryObject sortEntry = new QueryObject();
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
  public IQueryObject build(QueryFieldListener fieldListener, ElasticIndexConfig indexConfig, QueryParams params) {
    IQueryObject query = new QueryObject();
    IQueryObject queryContainer = new QueryObject();

    boolean useSearchAfterMode = useSearchAfter(indexConfig, params);
    IQueryObject sort = buildSortOrder(params.tryGetResultFilter().getSort(), indexConfig, useSearchAfterMode);

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
      IQueryObject boolQuery = mapCondition(0, fieldListener, condition, null, null);
      queryContainer.put("query", boolQuery);
    } else {
      queryContainer.put("query", new MatchAll());
    }

    ResultFilter.ContentFilter filter = params.tryGetResultContentFilter();
    if (filter != null) {
      QueryObject source = new QueryObject();
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
      QueryObject aggregations = new QueryObject();
      for (Aggregation agg : params.getAggregate()) {
        if (agg instanceof Aggregation.Wrapper) {
          // unwrap Aggregation
          agg = ((Aggregation.Wrapper) agg).iterator().next();
        }
        IQueryObject aggObj = buildAggregation(fieldListener, agg);
        aggregations.put(agg.getName(), aggObj);
      }
      queryContainer.put("aggs", aggregations);
    }

    return queryContainer;
  }


  public IQueryObject build(QueryFieldListener fieldListener, QueryParams params) {
    return build(fieldListener, null, params);
  }

  public IQueryObject build(QueryParams params) {
    return build(null, null, params);
  }
}
