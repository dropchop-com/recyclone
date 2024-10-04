package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.model.api.query.Aggregation;
import com.dropchop.recyclone.model.api.query.aggregation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.*;

public class AggregationDeserializer extends JsonDeserializer<List<Aggregation>> {

  private List<AggregationContainer> convertToAggregationContainer(List<Aggregation> aggs) {
    return aggs.stream().map(AggregationContainer::new).toList();
  }

  private Aggregation aggregationSwitch(
    Class<? extends Aggregation> cClass,
    String name, String field,
    List<Aggregation> subAggregations,
    Map.Entry<String, JsonNode> entry) {

    List<AggregationContainer> subAggregation = convertToAggregationContainer(subAggregations);

    switch (cClass.getSimpleName()) {
      case "Max":
        if(subAggregation.isEmpty()) {
          return new Max(name, field);
        }
        return new Max(name, field, subAggregation);
      case "Min":
        if(subAggregation == null) {
          return new Min(name, field);
        }
        return new Min(name, field, subAggregation);
      case "Sum":
        if(subAggregation == null) {
          return new Sum(name, field);
        }
        return new Sum(name, field, subAggregation);
      case "Avg":
        if(subAggregation == null) {
          return new Avg(name, field);
        }
        return new Avg(name, field, subAggregation);
      case "Count":
        if(subAggregation == null) {
          return new Count(name, field);
        }
        return new Count(name, field, subAggregation);
      case "Cardinality":
        if(subAggregation == null) {
          return new Cardinality(name, field);
        }
        return new Cardinality(name, field, subAggregation);
      case "Terms":
        if(subAggregation == null) {
          return new Terms(name, field);
        }
        return new Terms(name, field, subAggregation);
      case "DateHistogram":
        String interval = entry.getValue().get("calendar_interval").toString();
        interval = interval.substring(1, interval.length()-1);
        if(subAggregation == null) {
          return new DateHistogram(name, field, interval);
        }
        return new DateHistogram(name, field, interval, subAggregation);
    }
    return null;
  }

  private Aggregation deserializeStep(Map.Entry<String, JsonNode> entry) {
    String type = entry.getKey().substring(1);
    Class<? extends Aggregation> cClass = Aggregation.supported().get(type);

    if(cClass != null) {
      String name = entry.getValue().get("name").toString();
      name = name.substring(1, name.length()-1);
      String field = entry.getValue().get("field").toString();
      field = field.substring(1, field.length()-1);
      JsonNode aggs = entry.getValue().get("aggs");

      List<Aggregation> subAggregation = new ArrayList<>();

      if(!aggs.isEmpty()) {
        for(JsonNode obType : aggs ) {
          subAggregation.add(deserializeStep(obType.fields().next()));
        }
      }

      return aggregationSwitch(cClass, name, field, subAggregation, entry);

    } else {
      throw new IllegalArgumentException("Invalid query structure at [" + type + "]!");
    }
  }

  @Override
  public List<Aggregation> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    ObjectCodec codec = jsonParser.getCodec();
    JsonNode node = codec.readTree(jsonParser);
    List<Aggregation> aggregations = new ArrayList<>();
    for(JsonNode ob : node) {
      aggregations.add(deserializeStep(ob.fields().next()));
    }

    return aggregations;
  }
}
