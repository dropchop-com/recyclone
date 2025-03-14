package com.dropchop.recyclone.base.es.repo.mapper;

import com.dropchop.recyclone.base.api.model.query.*;
import com.dropchop.recyclone.base.api.model.query.aggregation.*;
import com.dropchop.recyclone.base.api.model.query.condition.*;
import com.dropchop.recyclone.base.api.model.query.operator.*;
import com.dropchop.recyclone.base.api.model.query.operator.text.Phrase;
import com.dropchop.recyclone.base.api.model.query.operator.text.Wildcard;
import com.dropchop.recyclone.base.api.repo.mapper.BoolQueryObject;
import com.dropchop.recyclone.base.api.repo.mapper.OperatorNodeObject;
import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;
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
      end.put("query", bool);
    }

    if(params.getAggregation() != null) {
      QueryNodeObject aggregations = new QueryNodeObject();
      for(Aggregation agg : params.getAggregation()) {
        if(agg instanceof Aggregation.Wrapper) {
          aggregations.put(agg.getName(), mapAggregation(((Aggregation.Wrapper) agg).iterator().next()));
        } else {
          aggregations.put(agg.getName(), mapAggregation(agg));
        }
      }
      end.put("aggs", aggregations);
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
    } else if (condition instanceof Wildcard<?> wildcard) {
      QueryNodeObject wildcardObject = new QueryNodeObject();
      QueryNodeObject nameObject = new QueryNodeObject();
      QueryNodeObject valueObject = new QueryNodeObject();
      valueObject.put("value", wildcard.getValue());
      QueryNodeObject boostObject = new QueryNodeObject();
      boostObject.put("boost", wildcard.getBoost());
      QueryNodeObject caseObject = new QueryNodeObject();
      caseObject.put("case_insensitive", wildcard.getCaseInsensitive());

      QueryNodeObject paramsObject = new QueryNodeObject();
      paramsObject.putAll(valueObject);
      paramsObject.putAll(boostObject);
      paramsObject.putAll(caseObject);

      nameObject.put(wildcard.getName().toString(), paramsObject);
      wildcardObject.put("wildcard", nameObject);
      return wildcardObject;
    } else if (condition instanceof Phrase<?> phrase) {
      QueryNodeObject phraseObject = new QueryNodeObject();
      QueryNodeObject nameObject = new QueryNodeObject();
      QueryNodeObject paramsObject = new QueryNodeObject();
      QueryNodeObject valueObject = new QueryNodeObject();

      valueObject.put("query", phrase.getValue());
      paramsObject.putAll(valueObject);

      if(phrase.getAnalyzer() != null) {
        QueryNodeObject anaObject = new QueryNodeObject();
        anaObject.put("analyzer", phrase.getAnalyzer());
        paramsObject.putAll(anaObject);
      }

      if(phrase.getSlop() != 0) {
        QueryNodeObject caseObject = new QueryNodeObject();
        caseObject.put("slop", phrase.getSlop());
        paramsObject.putAll(caseObject);
      }

      nameObject.put(phrase.getName().toString(), paramsObject);
      phraseObject.put("match_phrase", nameObject);
      return phraseObject;
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

  public QueryNodeObject mapAggregation(Aggregation aggregation) {
    QueryNodeObject node = new QueryNodeObject();

    if (aggregation instanceof Terms terms) {
      QueryNodeObject termsNode = new QueryNodeObject();
      termsNode.put("field", terms.getField());

      if(terms.getSize() != null) {
        termsNode.put("size", terms.getSize());
      }

      node.put("terms", termsNode);
    }
    else if (aggregation instanceof DateHistogram dh) {
      QueryNodeObject dhNode = new QueryNodeObject();
      dhNode.put("field", dh.getField());
      dhNode.put("calendar_interval", dh.getCalendar_interval());

      node.put("date_histogram", dhNode);
    }
    else if (aggregation instanceof Avg) {
      QueryNodeObject avg = new QueryNodeObject();
      avg.put("field", aggregation.getField());
      node.put("avg", avg);
    }
    else if (aggregation instanceof Count) {
      QueryNodeObject count = new QueryNodeObject();
      count.put("field", aggregation.getField());
      node.put("value_count", count);
    }
    else if (aggregation instanceof Max) {
      QueryNodeObject max = new QueryNodeObject();
      max.put("field", aggregation.getField());
      node.put("max", max);
    }
    else if (aggregation instanceof Min) {
      QueryNodeObject min = new QueryNodeObject();
      min.put("field", aggregation.getField());
      node.put("min", min);
    }
    else if (aggregation instanceof Sum) {
      QueryNodeObject sum = new QueryNodeObject();
      sum.put("field", aggregation.getField());
      node.put("sum", sum);
    }
    else if(aggregation instanceof Cardinality) {
      QueryNodeObject cardinality = new QueryNodeObject();
      cardinality.put("field", aggregation.getField());
      node.put("cardinality", cardinality);
    }
    else if(aggregation instanceof Stats) {
      QueryNodeObject stats = new QueryNodeObject();
      stats.put("field", aggregation.getField());
      node.put("stats", stats);
    }

    if (aggregation instanceof BucketAggregation bucket) {
      if(bucket.getAggs() != null && !bucket.getAggs().isEmpty()) {
        QueryNodeObject subAggs = new QueryNodeObject();
        for (Aggregation sub : bucket.getAggs()) {
          if(sub instanceof Aggregation.Wrapper) {
            subAggs.put(sub.getName(), mapAggregation(((Aggregation.Wrapper) sub).iterator().next()));
          } else {
            subAggs.put(sub.getName(), mapAggregation(sub));
          }
        }
        node.put("aggs", subAggs);
      }
    }

    return node;
  }
}
