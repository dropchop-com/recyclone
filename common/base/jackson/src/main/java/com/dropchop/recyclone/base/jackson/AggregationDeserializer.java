package com.dropchop.recyclone.base.jackson;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import com.dropchop.recyclone.base.api.model.query.aggregation.AggregationList;
import com.dropchop.recyclone.base.api.model.query.aggregation.DateHistogram;
import com.dropchop.recyclone.base.api.model.query.aggregation.Terms;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;

public class AggregationDeserializer extends JsonDeserializer<AggregationList> {

  private Aggregation aggregationSwitch(
    Class<? extends Aggregation> cClass,
    String name, String field,
    AggregationList subAggregations,
    Map.Entry<String, JsonNode> entry) throws Exception {

    try {
      Constructor<? extends Aggregation> constructor;

      if(cClass.equals(DateHistogram.class)) {
        String interval = entry.getValue().get("calendar_interval").asText();

        if(subAggregations.isEmpty()) {
          return new DateHistogram(name, field, interval);
        }
        return new DateHistogram(name, field, interval, subAggregations);
      } else if(cClass.equals(Terms.class)) {
        JsonNode sizeNode = entry.getValue().get("size");

        if(sizeNode != null) {
          Integer size = sizeNode.asInt();
          if(subAggregations.isEmpty()) {
            return new Terms(name, field, size);
          }
          return new Terms(name, field, size, subAggregations);
        }
      }

      if(subAggregations.isEmpty()) {
        constructor = cClass.getConstructor(String.class, String.class);
        return constructor.newInstance(name, field);
      }

      constructor = cClass.getConstructor(String.class, String.class, AggregationList.class);
      return constructor.newInstance(name, field, subAggregations);
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

      AggregationList aggregations = new AggregationList();

      if(aggs != null && !aggs.isEmpty()) {
        for(JsonNode obType : aggs ) {
          aggregations.add(deserializeStep(obType.fields().next()));
        }
      }

      return aggregationSwitch(cClass, name, field, aggregations, entry);

    } else {
      throw new IllegalArgumentException("Invalid query structure at [" + type + "]!");
    }
  }

  @Override
  public AggregationList deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
    ObjectCodec codec = jsonParser.getCodec();
    JsonNode node = codec.readTree(jsonParser);
    AggregationList aggregations = new AggregationList();

    for(JsonNode ob : node) {
      try {
        aggregations.add(deserializeStep(ob.fields().next()));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    return new AggregationList(aggregations);
  }
}
