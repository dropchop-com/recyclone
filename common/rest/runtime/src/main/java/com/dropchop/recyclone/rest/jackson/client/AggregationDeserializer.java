package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.model.api.query.Aggregation;
import com.dropchop.recyclone.model.api.query.aggregation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class AggregationDeserializer extends JsonDeserializer<AggregationWrappers> {

  private List<AggregationWrapper> convertToAggregationContainer(List<Aggregation> aggs) {
    return aggs.stream().map(AggregationWrapper::new).toList();
  }

  private Aggregation aggregationSwitch(
    Class<? extends Aggregation> cClass,
    String name, String field,
    List<Aggregation> subAggregations,
    Map.Entry<String, JsonNode> entry) throws Exception {

    List<AggregationWrapper> subAggregation = convertToAggregationContainer(subAggregations);

    try {
      Constructor<? extends Aggregation> constructor;

      if(cClass.equals(DateHistogram.class)) {
        String interval = entry.getValue().get("calendar_interval").asText();

        if(subAggregation.isEmpty()) {
          return new DateHistogram(name, field, interval);
        }
        return new DateHistogram(name, field, interval, subAggregation);
      }

      if(subAggregation.isEmpty()) {
        constructor = cClass.getConstructor(String.class, String.class);
        return constructor.newInstance(name, field);
      }

      constructor = cClass.getConstructor(String.class, String.class, List.class);
      return constructor.newInstance(name, field, subAggregation);
    } catch (NoSuchMethodException e) {
      throw new NoSuchMethodException("Constructor with the specified signature not found in " + e);
    } catch (Exception e) {
      throw new Exception("Error creating instance of " + cClass.getSimpleName());
    }
  }

  private Aggregation deserializeStep(Map.Entry<String, JsonNode> entry) throws Exception {
    String type = entry.getKey().substring(1);
    Class<? extends Aggregation> cClass = Aggregation.supported().get(type);

    if(cClass != null) {
      String name = entry.getValue().get("name").asText();
      String field = entry.getValue().get("field").asText();
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
  public AggregationWrappers deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
    ObjectCodec codec = jsonParser.getCodec();
    JsonNode node = codec.readTree(jsonParser);
    List<Aggregation> aggregations = new ArrayList<>();
    for(JsonNode ob : node) {
      try {
        aggregations.add(deserializeStep(ob.fields().next()));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    return convertToAggregationContainer(aggregations)
        .stream()
        .collect(
            Collectors.toCollection(AggregationWrappers::new)
        );
  }
}
