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

  private InputStream getTestResource() {
    InputStream is = getClass().getResourceAsStream("/elasticsearch-resp.json");
    assertNotNull(is, "Test resource " + "/elasticsearch-resp.json" + " not found!");
    return is;
  }

  @Test
  void parse() throws IOException {
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

    try (InputStream is = getTestResource()) {
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
}