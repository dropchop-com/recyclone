package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.model.api.query.Aggregation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.dropchop.recyclone.model.api.query.Aggregation.*;

public class AggregationDeserializerTest {

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
