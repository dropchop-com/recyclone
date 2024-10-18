package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.model.api.query.aggregation.AggregationList;
import com.dropchop.recyclone.model.api.query.Aggregation.Wrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.dropchop.recyclone.model.api.query.Aggregation.Wrapper.*;

public class AggregationDeserializerTest {

  @Test
  public void testAggregationCompositionTest() throws Exception {
    AggregationList a = Wrapper.aggs(
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
    String expected = """
        [
         {
           "$max": {
             "aggs": [
               {
                 "$sum": {
                   "aggs": [],
                   "name": "nested_worker_sum",
                   "field": "worker"
                 }
               },
               {
                 "$min": {
                   "aggs": [],
                   "name": "nested_worker_min",
                   "field": "worker"
                 }
               },
               {
                 "$avg": {
                   "aggs": [],
                   "name": "nested_worker_avg",
                   "field": "worker"
                 }
               },
               {
                 "$count": {
                   "aggs": [],
                   "name": "nested_nested_worker_count",
                   "field": "worker"
                 }
               }
             ],
             "name": "watch_max",
             "field": "watch"
           }
         },
         {
           "$cardinality": {
             "aggs": [],
             "name": "nested_nested_worker_cardinality",
             "field": "worker"
           }
         },
         {
           "$dateHistogram": {
             "aggs": [],
             "name": "nested_nested_worker_dateHistogram",
             "field": "worker",
             "calendar_interval": "month"
           }
         },
         {
           "$terms": {
             "aggs": [],
             "name": "nested_worker_terms",
             "field": "worker"
           }
         }
       ]""";
    JSONAssert.assertEquals(expected, jsonOutput1, true);
  }

  @Test
  @SuppressWarnings("unused")
  public void testAggregationDeserialization() throws Exception {
    AggregationList a = aggs(
      max(
        "price_max",
        "price",
        dateHistogram(
          "price_histogram",
          "price",
          "month"
        )
      )
    );

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(a);
    AggregationList a1 = mapper.readValue(jsonOutput1, AggregationList.class);
    a1 = Wrapper.wrap(a1);

    String jsonOutput2 = mapper.writeValueAsString(a1);
    JSONAssert.assertEquals(jsonOutput1, jsonOutput2, true);
  }

  @Test
  @SuppressWarnings("unused")
  public void testNestedAggregationDeserialization() throws Exception {
    AggregationList a = aggs(
      max(
        "price_max",
        "price",
        dateHistogram(
          "price_histogram",
          "price",
          "month"
        )
      ),
      min(
        "price_min",
        "price"
      )
    );

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(a);
    AggregationList a1 = mapper.readValue(jsonOutput1, AggregationList.class);
    a1 = Wrapper.wrap(a1);

    String jsonOutput2 = mapper.writeValueAsString(a1);
    JSONAssert.assertEquals(jsonOutput1, jsonOutput2, true);
  }

  @Test
  @SuppressWarnings("unused")
  public void completeAggregationDeserialization() throws Exception {
    AggregationList a = aggs(
      max(
        "price_max",
        "price",
        dateHistogram(
          "price_histogram",
          "price",
          "month"
        ),
        count(
          "price_count",
          "price"
        ),
        avg(
          "price_avg",
          "price",
          cardinality(
            "price_cardinality",
            "price",
            terms(
              "price_terms",
              "price"
            )
          )
        )
      ),
      min(
        "price_min",
        "price",
        dateHistogram(
          "price_histogram",
          "price",
          "seconds",
          sum(
            "price_sum",
            "price"
          )
        )
      )
    );

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(a);
    AggregationList a1 = mapper.readValue(jsonOutput1, AggregationList.class);
    a1 = Wrapper.wrap(a1);

    String jsonOutput2 = mapper.writeValueAsString(a1);
    JSONAssert.assertEquals(jsonOutput1, jsonOutput2, true);
  }
}
