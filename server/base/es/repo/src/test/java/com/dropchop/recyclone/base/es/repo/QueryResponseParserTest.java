package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.es.repo.QueryResponseParser.SearchResultMetadata;
import com.dropchop.recyclone.base.es.repo.listener.AggregationResultListener;
import com.dropchop.recyclone.base.es.repo.listener.QueryResultListener;
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

  @lombok.SneakyThrows
  private InputStream getTestResource() {
    InputStream is = getClass().getResourceAsStream("/elasticsearch-resp.json");
    assertNotNull(is, "Test resource " + "/elasticsearch-resp.json" + " not found!");
    return is;
  }

  @Test
  void parse() throws IOException {
    Map<String, Object> aggResult = new HashMap<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    Map<String, ?> result = new HashMap<>();
    List<Map<String, ?>> results = new ArrayList<>();
    QueryResultListener<Map<String, ?>> listener = (r) -> {
      results.add(r);
      return QueryResultListener.Progress.CONTINUE;
    };
    ObjectMapper mapper = new ObjectMapper();
    AggregationResultListener aggListener = aggResult::put;

    try (InputStream is = getTestResource()) {
      QueryResponseParser parser = new QueryResponseParser(mapper);
      @SuppressWarnings("unchecked")
      SearchResultMetadata metadata = parser.parse(
          is,
          (Class<Map<String,?>>) result.getClass(),
          List.of(listener),
          List.of(aggListener)
      );
      assertNotNull(metadata);
      assertEquals(668659, metadata.getTotalHits());
      assertEquals(10, metadata.getHits());
      assertEquals(10, results.size());
      assertEquals(1, aggResult.size());
      @SuppressWarnings("unchecked")
      Map<String, ?> agg = (Map<String,?>)aggResult.get("media_0_uuid_LFN_Media");
      assertEquals(4, agg.size());
    }
  }
}