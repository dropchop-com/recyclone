package com.dropchop.recyclone.repo.es.mapper;

import com.dropchop.recyclone.model.api.query.*;
import com.dropchop.recyclone.model.api.query.aggregation.AggregationList;
import com.dropchop.recyclone.model.api.query.aggregation.BaseAggregation;
import com.dropchop.recyclone.model.api.query.aggregation.Count;
import com.dropchop.recyclone.model.api.query.aggregation.DateHistogram;
import com.dropchop.recyclone.model.api.query.condition.And;
import com.dropchop.recyclone.model.api.query.condition.LogicalCondition;
import com.dropchop.recyclone.model.api.query.condition.Not;
import com.dropchop.recyclone.model.api.query.condition.Or;
import com.dropchop.recyclone.model.api.query.operator.*;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;

import java.util.Iterator;

@SuppressWarnings({"IfCanBeSwitch", "unused"})
public class ElasticQueryMapper {

  public static QueryNodeObject elasticQueryMapper(QueryParams params) {
    QueryNodeObject conditions = mapCondition(params.getCondition(), null);
    QueryNodeObject aggregations = mapAggregation(params.getAggregation());
    QueryNodeObject bool = new QueryNodeObject();
    bool.put("bool",conditions);

    QueryNodeObject end = new QueryNodeObject();
    end.put("query", bool);
    end.putAll(aggregations);
    return end;
  }

  public static QueryNodeObject mapCondition(Condition condition, BoolQueryObject previousCondition) {
    if (condition instanceof LogicalCondition logicalCondition) {
      BoolQueryObject query = new BoolQueryObject();

      for (Iterator<Condition> it = logicalCondition.iterator(); it.hasNext(); ) {
        Condition subCondition = it.next();
        if (condition instanceof And) {
          query.must(mapCondition(subCondition, new BoolQueryObject()));
        } else if (condition instanceof Or) {
          query.should(mapCondition(subCondition, new BoolQueryObject()));
        } else if (condition instanceof Not) {
          query.mustNot(mapCondition(subCondition, new BoolQueryObject()));
        }
      }
      return query;
    } else if (condition instanceof ConditionedField conditionedField) {
      String fieldName = conditionedField.getName();
      ConditionOperator operator = (ConditionOperator) conditionedField.values().toArray()[0];

      return mapConditionField(fieldName, operator);
    }

    return previousCondition;
  }

  public static OperatorNodeObject mapConditionField(String field, ConditionOperator operator) {
    OperatorNodeObject operatorNode = new OperatorNodeObject();

    if (operator == null) {
      return operatorNode;
    }

    if (operator instanceof Eq) {
      operatorNode.addEqOperator(field, ((Eq<?>) operator).get$eq());
    } else if (operator instanceof Gt) {
      operatorNode.addRangeOperator(field, "gt", ((Gt<?>) operator).get$gt());
    } else if (operator instanceof Lt) {
      operatorNode.addRangeOperator(field, "lt", ((Lt<?>) operator).get$lt());
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
    } else {
      throw new IllegalArgumentException("Unsupported ConditionOperator type: " + operator.getClass());
    }

    return operatorNode;
  }

  public static QueryNodeObject mapAggregation(AggregationList aggList) {
    QueryNodeObject query = new QueryNodeObject();
    QueryNodeObject aggsList = new QueryNodeObject();

    for (Aggregation aggregation : aggList) {
      QueryNodeObject aggregationObject = mapAggregationStep(aggregation);
      if (aggregationObject != null) {
        aggsList.putAll(aggregationObject);
      }
    }

    query.put("aggs", aggsList);
    return query;
  }

  protected static QueryNodeObject mapAggregationStep(Aggregation agg) {
    if (agg instanceof Aggregation.Wrapper) {
      Aggregation baseAggregation = ((Aggregation.Wrapper) agg).iterator().next();
      QueryNodeObject subQuery = new QueryNodeObject();
      QueryNodeObject field = new QueryNodeObject();
      QueryNodeObject endQuery = new QueryNodeObject();

      String aggregationType = setCorrectAggregationType(baseAggregation);

      field.put("field", baseAggregation.getField());

      if(baseAggregation instanceof DateHistogram) {
        QueryNodeObject calendarInterval = new QueryNodeObject();
        calendarInterval.put("calendar_interval", ((DateHistogram) baseAggregation).getCalendar_interval());
        field.putAll(calendarInterval);
      }

      subQuery.put(aggregationType, field);
      endQuery.put(baseAggregation.getName(), subQuery);

      if (baseAggregation instanceof BaseAggregation) {
        Iterator<Aggregation> aggregationIterator = ((BaseAggregation) baseAggregation).iterator();
        if (aggregationIterator.hasNext()) {
          QueryNodeObject subAggs = new QueryNodeObject();
          while (aggregationIterator.hasNext()) {
            Aggregation subAgg = aggregationIterator.next();
            QueryNodeObject subAggObject = mapAggregationStep(subAgg);
            if (subAggObject != null) {
              subAggs.putAll(subAggObject);
            }
          }

          subQuery.put("aggs", subAggs);
        }
      }

      return endQuery;
    }

    return null;
  }

  protected static String setCorrectAggregationType(Aggregation aggregation) {
    String className = aggregation.getClass().getSimpleName().toLowerCase();

    if(aggregation instanceof Count) {
      className = "value_count";
    } else if(aggregation instanceof DateHistogram) {
      className = "date_histogram";
    }

    return className;
  }
}
