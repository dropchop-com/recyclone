package com.dropchop.recyclone.repo.es.mapper;

import com.dropchop.recyclone.base.api.model.query.*;
import com.dropchop.recyclone.base.api.model.query.operator.*;
import com.dropchop.recyclone.base.api.model.query.aggregation.AggregationList;
import com.dropchop.recyclone.base.api.model.query.aggregation.BaseAggregation;
import com.dropchop.recyclone.base.api.model.query.aggregation.Count;
import com.dropchop.recyclone.base.api.model.query.aggregation.DateHistogram;
import com.dropchop.recyclone.base.api.model.query.condition.And;
import com.dropchop.recyclone.base.api.model.query.condition.LogicalCondition;
import com.dropchop.recyclone.base.api.model.query.condition.Not;
import com.dropchop.recyclone.base.api.model.query.condition.Or;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.Iterator;

@Slf4j
@ApplicationScoped
@SuppressWarnings({"IfCanBeSwitch", "unused"})
public class ElasticQueryMapper {

  public ElasticQueryMapper() {

  }

  public QueryNodeObject mapToString(QueryParams params) {
    QueryNodeObject bool = new QueryNodeObject();
    QueryNodeObject end = new QueryNodeObject();

    if(params.getCondition() != null) {
      QueryNodeObject conditions = mapCondition(params.getCondition(), null);
      bool.put("bool",conditions);
    }

    end.put("query", bool);

    if(params.getAggregation() != null) {
      QueryNodeObject aggregations = mapAggregation(params.getAggregation());
      end.putAll(aggregations);
    }

    return end;
  }

  protected QueryNodeObject mapCondition(Condition condition, BoolQueryObject previousCondition) {
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
    } else if (condition instanceof Field<?> field) {

      if(field.iterator().next() instanceof ZonedDateTime) {
        OperatorNodeObject operator = new OperatorNodeObject();
        if(field.iterator().next() instanceof ZonedDateTime date) {
          operator.addClosedInterval(field.getName(), date, date);
        }
        return operator;
      }

      if(field.iterator().next() == null) {
        OperatorNodeObject operator = new OperatorNodeObject();
        operator.addNullSearch(field.getName());
        return operator;
      }

      QueryNodeObject mustWrapper = new QueryNodeObject();
      QueryNodeObject query = new QueryNodeObject();
      QueryNodeObject queryWrapper = new QueryNodeObject();
      query.put(field.getName(), field.iterator().next());
      queryWrapper.put("term", query);
      mustWrapper.put("must", queryWrapper);

      if(previousCondition == null) {
        return mustWrapper;
      }

      return queryWrapper;
    }

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
    } else if (operator == null) {
      operatorNode.addNullSearch(field);
    } else {
      operatorNode.addEqOperator(field, operator);
    }

    return operatorNode;
  }

  protected QueryNodeObject mapAggregation(AggregationList aggList) {
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

  protected QueryNodeObject mapAggregationStep(Aggregation agg) {
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

  protected String setCorrectAggregationType(Aggregation aggregation) {
    String className = aggregation.getClass().getSimpleName().toLowerCase();

    if(aggregation instanceof Count) {
      className = "value_count";
    } else if(aggregation instanceof DateHistogram) {
      className = "date_histogram";
    }

    return className;
  }
}
