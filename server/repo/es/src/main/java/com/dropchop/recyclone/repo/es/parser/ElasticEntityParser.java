package com.dropchop.recyclone.repo.es.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ElasticEntityParser {

  private final ObjectMapper objectMapper;

  public ElasticEntityParser(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T> List<T> parseResponse(String response, Class<T> entityClass) throws IOException {
    JsonNode rootNode = objectMapper.readTree(response);
    JsonNode hitsNode = rootNode.path("hits").path("hits");

    List<T> entities = new ArrayList<>();
    for (JsonNode hit : hitsNode) {
      JsonNode source = hit.path("_source");
      T entity = parseHit(source, entityClass);
      entities.add(entity);
    }

    return entities;
  }

  private <T> T parseHit(JsonNode source, Class<T> entityClass) throws IOException {
    try {
      return objectMapper.treeToValue(source, entityClass);
    } catch (Exception e) {
      throw new IOException("Error parsing entity", e);
    }
  }
}
