package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.model.api.query.Aggregation;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class AggregationDeserializer extends JsonDeserializer<Aggregation> {

  @Override
  public Aggregation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
    ObjectCodec codec = jsonParser.getCodec();
    JsonNode node = codec.readTree(jsonParser);
    Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
    if(iterator.hasNext()) {
      Map.Entry<String, JsonNode> entry = iterator.next();

    }

    return null;
  }
}
