package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.dto.model.rest.AggregationResult;
import com.dropchop.recyclone.base.es.repo.QueryResponseParser.SearchResultMetadata;
import com.dropchop.recyclone.base.es.repo.listener.AggregationResultConsumer;
import com.dropchop.recyclone.base.es.repo.listener.QueryResultConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QueryResponseParserTest {

  private InputStream getTestResource(String resourceName) {
    InputStream is = getClass().getResourceAsStream("/" + resourceName);
    assertNotNull(is, "Test resource " + resourceName + " not found!");
    return is;
  }

  @Test
  void parse1() throws IOException {
    Map<String, AggregationResult> aggResult = new HashMap<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    Map<String, ?> result = new HashMap<>();
    List<Map<String, ?>> results = new ArrayList<>();
    QueryResultConsumer<Map<String, ?>> listener = (r) -> {
      results.add(r);
      return QueryResultConsumer.Progress.CONTINUE;
    };

    ObjectMapper mapper = new ObjectMapper();
    AggregationResultConsumer aggListener = aggResult::put;

    try (InputStream is = getTestResource("elasticsearch-resp-1.json")) {
      QueryResponseParser parser = new QueryResponseParser(mapper);
      @SuppressWarnings("unchecked")
      SearchResultMetadata metadata = parser.parse(
          is,
          new QueryParams(),
          (Class<Map<String, ?>>) result.getClass(),
          List.of(listener),
          List.of(aggListener)
      );
      assertNotNull(metadata);
      assertEquals(668659, metadata.getTotalHits());
      assertEquals(10, metadata.getHits());
      assertEquals(10, results.size());
      assertEquals(1, aggResult.size());
      AggregationResult subAgg = aggResult.get("media_0_uuid_LFN_Media");
      assertEquals(1500, subAgg.getContainers().size());
      String json = mapper.writeValueAsString(aggResult);
      assertFalse(json.isBlank());
    }
  }


  @Test
  void parse2() throws IOException {
    Map<String, AggregationResult> aggResult = new HashMap<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    Map<String, ?> result = new HashMap<>();
    QueryResultConsumer<Map<String, ?>> listener = (r) -> QueryResultConsumer.Progress.CONTINUE;

    ObjectMapper mapper = new ObjectMapper();
    AggregationResultConsumer aggListener = aggResult::put;

    try (InputStream is = getTestResource("elasticsearch-resp-2.json")) {
      QueryResponseParser parser = new QueryResponseParser(mapper);
      @SuppressWarnings("unchecked")
      SearchResultMetadata metadata = parser.parse(
          is,
          new QueryParams(),
          (Class<Map<String, ?>>) result.getClass(),
          List.of(listener),
          List.of(aggListener)
      );
      assertNotNull(metadata);
      AggregationResult subAgg = aggResult.get("distributed");
      assertEquals(9, subAgg.getContainers().size());
      AggregationResult.Container container = subAgg.getContainers().get(7);
      assertEquals(3, container.getResults().size());
      subAgg = container.getResults().get("neutralTags");
      assertNotNull(subAgg);
      assertEquals(2, subAgg.getContainers().size());
      assertNotNull(subAgg.getContainers().getFirst());
      assertNotNull(subAgg.getContainers().getLast());
      String json = mapper.writeValueAsString(aggResult);
      assertFalse(json.isBlank());
    }
  }

  @Test
  void parse3() throws IOException {
    Map<String, AggregationResult> aggResult = new HashMap<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    Map<String, ?> result = new HashMap<>();
    QueryResultConsumer<Map<String, ?>> listener = (r) -> QueryResultConsumer.Progress.CONTINUE;

    ObjectMapper mapper = new ObjectMapper();
    AggregationResultConsumer aggListener = aggResult::put;

    try (InputStream is = getTestResource("elasticsearch-resp-3.json")) {
      QueryResponseParser parser = new QueryResponseParser(mapper);
      //noinspection unchecked
      parser.parse(
          is,
          new QueryParams(),
          (Class<Map<String, ?>>) result.getClass(),
          List.of(listener),
          List.of(aggListener)
      );
      AggregationResult subAgg = aggResult.get("publisher");
      AggregationResult.Container container = subAgg.getContainers().get(3);
      subAgg = container.getResults().get("neutralTags");
      assertEquals(3, subAgg.getContainers().size());
      String json = mapper.writeValueAsString(aggResult);
      assertFalse(json.isBlank());
    }
  }
}