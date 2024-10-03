package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.model.api.query.Aggregation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.List;

import static com.dropchop.recyclone.model.api.query.Aggregation.*;

public class AggregationDeserializerTest {

  @Test
  public void testAggregationCompositionTest() throws Exception {
    List<Aggregation> a = List.of(
      max(
        "watch_max",
        "watch",
        sum(
          "nested_worker_sum",
          "worker"
        ),
        min(
          "nested_worker_min",
          "worker"
        ),
        avg(
          "nested_worker_avg",
          "worker"
        ),
        count(
          "nested_nested_worker_count",
          "worker"
        )
      ),
      cardinality(
        "nested_nested_worker_cardinality",
        "worker"
      ),
      dateHistogram(
        "nested_nested_worker_dateHistogram",
        "worker",
        "month"
      ),
      terms(
        "nested_worker_terms",
        "worker"
      )
    );

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(a);
    System.out.printf(mapper.writeValueAsString(jsonOutput1));
    String expected = "[{\"name\":\"watch_max\",\"field\":\"watch\",\"$max\":[{\"name\":\"nested_worker_sum\",\"field\":\"worker\",\"$sum\":[]},{\"name\":\"nested_worker_min\",\"field\":\"worker\",\"$min\":[]},{\"name\":\"nested_worker_avg\",\"field\":\"worker\",\"$avg\":[]},{\"name\":\"nested_nested_worker_count\",\"field\":\"worker\",\"$count\":[]}]},{\"name\":\"nested_nested_worker_cardinality\",\"field\":\"worker\",\"$cardinality\":[]},{\"name\":\"nested_nested_worker_dateHistogram\",\"field\":\"worker\",\"calenderInterval\":\"month\",\"$dateHistogram\":[]},{\"name\":\"nested_worker_terms\",\"field\":\"worker\",\"$terms\":[]}]";
    JSONAssert.assertEquals(expected, jsonOutput1, true);
  }

  @Test
  public void testAggregationDeserialization() throws Exception {
    List<Aggregation> a = List.of(
      /*max(
        aggregationField("price_max", "price"),
        dateHistogram(
          aggregationHistogramField("price_histogram", "price", "month")
        )
      )*/
    );

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(a);
    List<Aggregation> a1 = mapper.readValue(jsonOutput1, new TypeReference<List<Aggregation>>() {});
  }
}
