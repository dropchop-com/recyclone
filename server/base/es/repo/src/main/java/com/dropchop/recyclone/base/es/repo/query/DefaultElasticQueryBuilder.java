package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.query.*;
import com.dropchop.recyclone.base.api.model.query.aggregation.*;
import com.dropchop.recyclone.base.api.model.query.aggregation.Terms;
import com.dropchop.recyclone.base.api.model.query.condition.*;
import com.dropchop.recyclone.base.api.model.query.operator.*;
import com.dropchop.recyclone.base.api.model.query.operator.Match;
import com.dropchop.recyclone.base.api.model.query.operator.text.AdvancedText;
import com.dropchop.recyclone.base.api.model.query.operator.text.Phrase;
import com.dropchop.recyclone.base.api.model.query.operator.text.Text;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.*;
import com.dropchop.recyclone.base.es.model.query.Sort;
import com.dropchop.recyclone.base.es.model.query.cond.*;
import com.dropchop.recyclone.base.es.model.query.cond.Knn;
import com.dropchop.recyclone.base.es.repo.config.ElasticIndexConfig;
import com.dropchop.recyclone.base.es.repo.config.HasDefaultSort;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;

import static com.dropchop.recyclone.base.es.model.query.SortField.Order.ASC;
import static com.dropchop.recyclone.base.es.model.query.SortField.Order.DESC;

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

  protected QueryObject _buildAggregation(int level, IQueryNode parent, QueryFieldListener listener,
                                          Aggregation aggregation) {
    if (aggregation == null) {
      return null;
    }

    if (aggregation instanceof Aggregation.Wrapper) {
      aggregation = ((Aggregation.Wrapper) aggregation).iterator().next();
    }

    QueryObject node;

    if (aggregation instanceof Terms terms) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Terms(
          parent, terms.getField(), terms.getSize(), terms.getShardSize(), terms.getFilter()
      );
    } else if (aggregation instanceof DateHistogram dh) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.DateHistogram(
          parent, dh.getField(), dh.getCalendarInterval(), dh.getTimeZone()
      );
    } else if (aggregation instanceof Avg) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Avg(parent, aggregation.getField());
    } else if (aggregation instanceof Count) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Count(parent, aggregation.getField());
    } else if (aggregation instanceof Max) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Max(parent, aggregation.getField());
    } else if (aggregation instanceof Min) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Min(parent, aggregation.getField());
    } else if (aggregation instanceof Sum) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Sum(parent, aggregation.getField());
    } else if (aggregation instanceof Cardinality) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Cardinality(parent, aggregation.getField());
    } else if (aggregation instanceof Stats) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.Stats(parent, aggregation.getField());
    } else if (aggregation instanceof com.dropchop.recyclone.base.api.model.query.aggregation.TopHits topHits) {
      node = new com.dropchop.recyclone.base.es.model.query.agg.TopHits(
          parent, topHits.getSize(), topHits.getSort(), topHits.getFilter()
      );
    } else {
      node = new QueryObject();
    }

    if (listener != null) {
      listener.on(level, aggregation, node);
    }

    if (aggregation instanceof BucketAggregation bucket) {
      if (bucket.getAggs() != null && !bucket.getAggs().isEmpty()
          && node instanceof com.dropchop.recyclone.base.es.model.query.agg.AggregationBucket bucketNode) {
        for (Aggregation sub : bucket.getAggs()) {
          if (sub instanceof Aggregation.Wrapper) {
            sub = ((Aggregation.Wrapper) sub).iterator().next();
          }
          IQueryObject subAggObj = _buildAggregation(level + 1, node, listener, sub);
          bucketNode.addAgg(sub.getName(), subAggObj);
        }
      }
    }

    return node;
  }

  @Override
  public QueryObject buildAggregation(QueryFieldListener listener, Aggregation aggregation) {
    return _buildAggregation(0, null, listener, aggregation);
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


  protected Sort buildSortOrder(IQueryNode parent, List<String> sortList, ElasticIndexConfig indexConfig,
                                boolean useSearchAfter) {
    Sort sortOrder;
    if (!sortList.isEmpty()) {
      sortOrder = new Sort(parent);
      for (String sort : sortList) {
        if (sort.startsWith("-")) {
          sortOrder.addSort(sort.substring(1), DESC);
        } else {
          if (sort.startsWith("+")) {
            sort = sort.substring(1);
          }
          sortOrder.addSort(sort, ASC);
        }
      }
    } else if (indexConfig instanceof HasDefaultSort hasDefaultSort) {
      sortOrder = hasDefaultSort.getSortOrder();
      sortOrder.setParent(parent);
      if ((sortOrder.isEmpty()) && useSearchAfter) {
        throw new ServiceException(
            ErrorCode.internal_error, "No sort order received from index config for deep pagination!"
        );
      }
      if (!sortOrder.isEmpty()) {
        return sortOrder;
      }
    }
    return null;
  }

  @Override
  public IQueryObject build(QueryFieldListener fieldListener, ElasticIndexConfig indexConfig, QueryParams params) {
    IQueryObject query = new QueryObject();
    IQueryObject queryBody = new QueryObject();

    boolean useSearchAfterMode = useSearchAfter(indexConfig, params);
    Sort sort = buildSortOrder(query, params.tryGetResultFilter().getSort(), indexConfig, useSearchAfterMode);

    int size = params.tryGetResultFilter().size();
    int from = params.tryGetResultFilter().from();

    if (useSearchAfterMode) {
      if (sort == null) {
        throw new ServiceException(
            ErrorCode.parameter_validation_error, "Sort must be defined when using search-after mode!"
        );
      }
      queryBody.put("size", size);
    } else {
      queryBody.put("from", from);
      queryBody.put("size", size);
    }

    if (sort != null) {
      queryBody.putAll(sort);
    }

    Condition condition = params.getCondition();
    if (hasActualConditions(condition)) {
      IQueryObject boolQuery = mapCondition(0, fieldListener, condition, null, null);
      queryBody.put("query", boolQuery);
    } else {
      queryBody.put("query", new MatchAll());
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
        queryBody.put("_source", source);
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
      queryBody.put("aggs", aggregations);
    }

    return queryBody;
  }

  public IQueryObject build(QueryFieldListener fieldListener, QueryParams params) {
    return build(fieldListener, null, params);
  }

  public IQueryObject build(QueryParams params) {
    return build(null, null, params);
  }
}
