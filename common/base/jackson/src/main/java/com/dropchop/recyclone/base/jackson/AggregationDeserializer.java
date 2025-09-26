package com.dropchop.recyclone.base.jackson;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import com.dropchop.recyclone.base.api.model.query.aggregation.*;
import com.dropchop.recyclone.base.api.model.query.operator.filter.Exclude;
import com.dropchop.recyclone.base.api.model.query.operator.filter.Filter;
import com.dropchop.recyclone.base.api.model.query.operator.filter.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class AggregationDeserializer extends JsonDeserializer<AggregationList> {

  List<String> convertToList(JsonNode inputNode) {
    List<String> list = new ArrayList<>();
    if (inputNode.isArray()) {
      for (JsonNode node : inputNode) {
        list.add(node.asText());
      }
    } else if (inputNode.isTextual()) {
      list.add(inputNode.asText());
    }
    return list;
    /*
    return Arrays.stream(input
        .replaceAll("^\\[|]$", "")
        .replaceAll("\"", "")
        .trim()
        .split("\\s*,\\s*"))
      .filter(s -> !s.isEmpty())
      .collect(Collectors.toList());
     */
  }

  private Aggregation aggregationSwitch(
    Class<? extends Aggregation> cClass,
    String name, String field,
    AggregationList subAggregations,
    Map.Entry<String, JsonNode> entry) throws Exception {

    try {
      Constructor<? extends Aggregation> constructor;

      if (cClass.equals(DateHistogram.class)) {
        String interval = entry.getValue().get("calendar_interval").asText();

        if (subAggregations.isEmpty()) {
          return new DateHistogram(name, field, interval);
        }
        return new DateHistogram(name, field, interval, subAggregations);
      } else if (cClass.equals(TopHits.class)) {
        JsonNode sizeNode = entry.getValue().get("size");
        JsonNode sortNode = entry.getValue().get("sort");
        Integer size = null;
        if (sizeNode != null) {
          size = sizeNode.asInt();
        }
        List<Sort> sortList = new ArrayList<>();
        if (sortNode != null && sortNode.isArray()) {
          for (JsonNode sortElement : sortNode) {
            String fieldName = sortElement.get("field").asText();
            String value = sortElement.get("value").asText();
            sortList.add(new Sort(fieldName, value));
          }
        }
        return new TopHits(name, size, sortList);
      } else if (cClass.equals(Terms.class)) {
        JsonNode sizeNode = entry.getValue().get("size");
        JsonNode filterNode = entry.getValue().get("filter");

        Terms terms = new Terms();

        terms.setName(name);
        terms.setField(field);

        Integer size = null;
        if (sizeNode != null) {
          size = sizeNode.asInt();
        }

        if (size != null) {
          if (subAggregations.isEmpty()) {
            terms.setSize(size);
          } else {
            terms.setSize(size);
            terms.setAggs(subAggregations);
          }
        }

        if (filterNode != null) {
          JsonNode includeNode = filterNode.get("include");
          JsonNode excludeNode = filterNode.get("exclude");

          if ((includeNode != null && !includeNode.isNull()) && (excludeNode != null && !excludeNode.isNull())) {
            terms.setFilter(
              new Filter(
                new Include(convertToList(includeNode.get("value"))),
                new Exclude(convertToList(excludeNode.get("value")))
              )
            );
          } else if (includeNode != null && !includeNode.isNull()) {
            terms.setFilter(new Filter(new Include(convertToList(includeNode.get("value")))));
          } else if (excludeNode != null && !excludeNode.isNull()) {
            terms.setFilter(new Filter(new Exclude(convertToList(excludeNode.get("value")))));
          }
        }

        return terms;
      }

      if (subAggregations.isEmpty()) {
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

    if (cClass != null) {
      String name = null;
      String field = null;
      if (entry.getValue().get("name") != null) {
        name = entry.getValue().get("name").asText();
      }
      if (entry.getValue().get("field") != null) {
        field = entry.getValue().get("field").asText();
      }
      JsonNode aggs = entry.getValue().get("aggs");

      AggregationList aggregations = new AggregationList();

      if (aggs != null && !aggs.isEmpty()) {
        for (JsonNode obType : aggs) {
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

    for (JsonNode ob : node) {
      try {
        aggregations.add(deserializeStep(ob.fields().next()));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    return new AggregationList(aggregations);
  }
}
