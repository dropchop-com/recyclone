package com.dropchop.recyclone.base.jackson;

import com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper;
import com.dropchop.recyclone.base.api.model.query.aggregation.AggregationList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.List;

import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.*;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.*;

public class AggregationDeserializerTest {

  @Test
  public void testAggregationCompositionTest() throws Exception {
    AggregationList a = Wrapper.aggs(
      max(
        "watch_max",
        "watch"
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
        "worker",
        10
      )
    );

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(a);
    String expected = """
        [
         {
           "$max": {
             "name": "watch_max",
             "field": "watch"
           }
         },
         {
           "$cardinality": {
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
             "field": "worker",
             "size": 10
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
  public void testNestedAggregationDeserialization() throws Exception {
    AggregationList a = aggs(
      max(
        "price_max",
        "price"
      ),
      min(
        "price_min",
        "price"
      ),
      terms(
        "nested_worker_terms",
        "worker",
        10
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
        "price"
      ),
      min(
        "price_min",
        "price"
      ),
      dateHistogram(
        "price_histogram",
        "price",
        "seconds",
        sum(
          "price_sum",
          "price"
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
  public void filterTermsAggregationTest() throws Exception {
    AggregationList a = aggs(
      dateHistogram(
        "price_histogram",
        "price",
        "seconds",
        terms(
          "price_sum",
          "price",
          filter(
            includes(List.of("include_ports")),
            excludes(List.of("*poms*"))
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

  @Test
  public void filterExcludeTermsAggregationTest() throws Exception {
    AggregationList a = aggs(
      dateHistogram(
        "price_histogram",
        "price",
        "seconds",
        terms(
          "price_sum",
          "price",
          filter(
            excludes(List.of("*poms*"))
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

  @Test
  public void filterIncludeTermsAggregationTest() throws Exception {
    AggregationList a = aggs(
      dateHistogram(
        "price_histogram",
        "price",
        "seconds",
        terms(
          "price_sum",
          "price",
          filter(
            includes(List.of("include_ports"))
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

  @Test
  public void noFilterTermsAggregationTest() throws Exception {
    AggregationList a = aggs(
      dateHistogram(
        "price_histogram",
        "price",
        "seconds",
        terms(
          "price_sum",
          "price",
          4500,
          terms(
            "terms with",
            "price",
            filter(
              includes(List.of("include_ports"))
            )
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
