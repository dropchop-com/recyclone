package com.dropchop.recyclone.model.es.dto.query;

import com.dropchop.recyclone.model.es.api.query.QueryAggregation;
import com.dropchop.recyclone.model.es.api.query.QueryFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.List;

public class ElasticSearchQuery extends Query {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public ElasticSearchQuery(List<QueryFilter<?>> filters, List<QueryAggregation> aggregations) {
    super(filters, aggregations);
  }

  public String buildJson() {
    ObjectNode rootNode = objectMapper.createObjectNode();
    ObjectNode queryNode = rootNode.putObject("query");
    ObjectNode boolNode = queryNode.putObject("bool");

    ArrayNode mustNode = boolNode.putArray("must");
    ArrayNode shouldNode = boolNode.putArray("should");
    ArrayNode mustNotNode = boolNode.putArray("must_not");

    for (QueryFilter<?> filter : getFilters()) {
      switch (filter.getLogicalOperator()) {
        case AND:
          mustNode.add(createFilterNode(filter));
          break;
        case OR:
          shouldNode.add(createFilterNode(filter));
          break;
        case NOT:
          mustNotNode.add(createFilterNode(filter));
          break;
      }
    }

    if (!getAggregation().isEmpty()) {
      ObjectNode aggsNode = rootNode.putObject("aggs");
      for (QueryAggregation aggregation : getAggregation()) {
        String aggName = aggregation.getType().name().toLowerCase() + "_" + aggregation.getField();
        aggsNode.set(aggName, createAggregationNode(aggregation));
      }
    }

    return rootNode.toString();
  }

  private ObjectNode createFilterNode(QueryFilter<?> filter) {
    ObjectNode filterNode = objectMapper.createObjectNode();
    switch (filter.getOperation()) {
      case EQUAL:
        filterNode.putObject("term").put(filter.getField(), objectMapper.valueToTree(filter.getValue()));
        break;
      case GREATER_THAN:
        filterNode.putObject("range").putObject(filter.getField()).put("gt", objectMapper.valueToTree(filter.getValue()));
        break;
      case LESS_THAN:
        filterNode.putObject("range").putObject(filter.getField()).put("lt", objectMapper.valueToTree(filter.getValue()));
        break;
      case RANGE:
        // the value is a List containing [lowerBound, upperBound]
        try {
          @SuppressWarnings("unchecked")
          List<Number> rangeValues = (List<Number>) filter.getValue();
          if (rangeValues.size() == 2) {
            ObjectNode rangeNode = filterNode.putObject("range").putObject(filter.getField());
            rangeNode.put("gte", objectMapper.valueToTree(rangeValues.get(0)));
            rangeNode.put("lte", objectMapper.valueToTree(rangeValues.get(1)));
          }
          break;
        } catch (ClassCastException e) {
          throw new ClassCastException("Filter value ["+filter.getValue()+"] is not a List of Numbers!");
        }
      case IN:
        ArrayNode valuesNode = objectMapper.createArrayNode();
        if (filter.getValue() instanceof Collection) {
          ((Collection<?>) filter.getValue()).forEach(v -> valuesNode.add( v.toString()));
        } else {
          valuesNode.add(filter.getValue().toString());
        }
        filterNode.putObject("terms").set(filter.getField(), valuesNode);
        break;
      case EXISTS:
        filterNode.putObject("exists").put("field", filter.getField());
        break;
      default:
        throw new IllegalArgumentException("Unsupported operation: " + filter.getOperation());
    }

    if (filter.getSubFilters() != null && !filter.getSubFilters().isEmpty()) {
      ArrayNode subFiltersNode = filterNode.putArray("filter");
      ObjectNode boolNode = subFiltersNode.addObject().putObject("bool");
      ArrayNode nestedMustNode = boolNode.putArray("must");
      filter.getSubFilters().forEach(subFilter -> nestedMustNode.add(createFilterNode(subFilter)));
    }

    return filterNode;
  }

  private ObjectNode createAggregationNode(QueryAggregation aggregation) {
    ObjectNode aggNode = objectMapper.createObjectNode();
    switch (aggregation.getType()) {
      case SUM:
        aggNode.putObject("sum").put("field", aggregation.getField());
        break;
      case AVG:
        aggNode.putObject("avg").put("field", aggregation.getField());
        break;
      case COUNT:
        aggNode.putObject("terms").put("field", aggregation.getField());
        break;
      case MAX:
        aggNode.putObject("max").put("field", aggregation.getField());
        break;
      case MIN:
        aggNode.putObject("min").put("field", aggregation.getField());
        break;
      default:
        throw new IllegalArgumentException("Unsupported aggregation type: " + aggregation.getType());
    }
    return aggNode;
  }
}
