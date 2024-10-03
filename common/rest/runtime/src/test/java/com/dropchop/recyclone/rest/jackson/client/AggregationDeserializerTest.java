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
            aggregationField("watch_max", "watch"),
            sum(
                aggregationField("nested_worker_sum", "worker")
            ),
            min(
                aggregationField("nested_worker_min", "worker")
            ),
            avg(
                aggregationField("nested_worker_avg", "worker"),
                count(
                    aggregationField("nested_nested_worker_count", "worker")
                ),
                cardinality(
                    aggregationField("nested_nested_worker_cardinality", "worker")
                ),
                dateHistogram(
                    aggregationHistogramField("nested_nested_worker_dateHistogram", "worker", "month")
                )
            )
        ),
        terms(
            aggregationField("worker_terms", "worker")
        )
    );

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(a);
    String expected = "";
    //JSONAssert.assertEquals(expected, jsonOutput1, false);
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
