package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.query.*;
import com.dropchop.recyclone.base.api.model.query.aggregation.*;
import com.dropchop.recyclone.base.api.model.query.condition.And;
import com.dropchop.recyclone.base.api.model.query.condition.LogicalCondition;
import com.dropchop.recyclone.base.api.model.query.condition.Not;
import com.dropchop.recyclone.base.api.model.query.condition.Or;
import com.dropchop.recyclone.base.api.model.query.operator.*;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.BoolQueryObject;
import com.dropchop.recyclone.base.es.model.query.OperatorNodeObject;
import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;
import com.dropchop.recyclone.base.es.repo.marker.AlwaysPresentDeleteFields;
import com.dropchop.recyclone.base.es.repo.marker.AlwaysPresentSearchFields;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
@ApplicationScoped
@SuppressWarnings({"IfCanBeSwitch", "unused"})
public class ElasticQueryBuilder {

  public ElasticQueryBuilder() {
  }

  protected QueryNodeObject mapCondition(Condition condition, BoolQueryObject parentCondition) {
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

      if (field.iterator().next() instanceof ZonedDateTime) {
        OperatorNodeObject operator = new OperatorNodeObject();
        if (field.iterator().next() instanceof ZonedDateTime date) {
          operator.addClosedInterval(field.getName(), date, date);
        }
        return operator;
      }

      if (field.iterator().next() == null) {
        OperatorNodeObject operator = new OperatorNodeObject();
        operator.addNullSearch(field.getName());
        return operator;
      }

      QueryNodeObject mustWrapper = new QueryNodeObject();
      QueryNodeObject query = new QueryNodeObject();
      QueryNodeObject termWrapper = new QueryNodeObject();
      query.put(field.getName(), field.iterator().next());
      termWrapper.put("term", query);
      mustWrapper.put("must", termWrapper);

      if (parentCondition == null) {
        return mustWrapper;
      }

      return termWrapper;
    }

    return parentCondition;
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

  protected void validateRequiredFields(QueryNodeObject query, Set<String> requiredFields, String operationType) {
    Set<String> rootFields = extractRootFieldNames(query);

    boolean hasRequiredField = rootFields.stream()
      .anyMatch(requiredFields::contains);

    if (!hasRequiredField) {
      throw new ServiceException(
        ErrorCode.internal_error,
        "Query validation failed: " + operationType + " operations require at least one of these fields " +
          "at the root level: " + String.join(", ", requiredFields) + "."
      );
    }
  }

  @SuppressWarnings("Java8MapApi")
  protected Set<String> extractRootFieldNames(QueryNodeObject query) {
    Set<String> fieldNames = new HashSet<>();

    if (query.containsKey("must")) {
      extractFieldsFromClause(query.get("must"), fieldNames);
    } else if (query.containsKey("should")) {
      extractFieldsFromClause(query.get("should"), fieldNames);
    } else {
      extractFieldsFromClause(query, fieldNames);
    }

    return fieldNames;
  }

  private void extractFieldsFromClause(Object clause, Set<String> fieldNames) {
    if (clause instanceof QueryNodeObject) {
      if (((QueryNodeObject) clause).containsKey("term")) {
        QueryNodeObject term = (QueryNodeObject) ((QueryNodeObject) clause).get("term");
        fieldNames.addAll(term.keySet());
      } else if (((QueryNodeObject) clause).containsKey("terms")) {
        QueryNodeObject range = (QueryNodeObject) ((QueryNodeObject) clause).get("terms");
        fieldNames.addAll(range.keySet());
      } else if (((QueryNodeObject) clause).containsKey("range")) {
        QueryNodeObject range = (QueryNodeObject) ((QueryNodeObject) clause).get("range");
        fieldNames.addAll(range.keySet());
      }

    } else if (clause instanceof List) {
      for (Object item : (List<?>) clause) {
        extractFieldsFromClause(item, fieldNames);
      }
    }
  }

  public QueryNodeObject build(QueryParams params, Object repository) {
    QueryNodeObject bool = new QueryNodeObject();
    QueryNodeObject end = new QueryNodeObject();

    if (params.getCondition() != null) {
      QueryNodeObject conditions = mapCondition(params.getCondition(), null);

      if (repository instanceof AlwaysPresentSearchFields) {
        validateRequiredFields(
          conditions,
          ((AlwaysPresentSearchFields) repository).anyOf(),
          "search"
        );
      } else if (repository instanceof AlwaysPresentDeleteFields) {
        validateRequiredFields(
          conditions,
          ((AlwaysPresentDeleteFields) repository).anyOf(),
          "delete"
        );
      }

      bool.put("bool", conditions);
      end.put("query", bool);
    }

    if (params.getAggregate() != null) {
      QueryNodeObject aggregations = new QueryNodeObject();
      for (Aggregation agg : params.getAggregate()) {
        if (agg instanceof Aggregation.Wrapper) {
          aggregations.put(agg.getName(), buildAggregation(((Aggregation.Wrapper) agg).iterator().next()));
        } else {
          aggregations.put(agg.getName(), buildAggregation(agg));
        }
      }
      end.put("aggs", aggregations);
    }

    return end;
  }

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
}
