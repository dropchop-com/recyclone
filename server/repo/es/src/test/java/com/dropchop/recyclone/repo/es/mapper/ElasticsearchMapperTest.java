package com.dropchop.recyclone.repo.es.mapper;

import com.dropchop.recyclone.model.api.query.Condition;
import com.dropchop.recyclone.model.api.query.aggregation.AggregationList;
import com.dropchop.recyclone.model.api.utils.Iso8601;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.dropchop.recyclone.rest.jackson.client.ObjectMapperFactory;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONException;
import org.junit.Test;

import static com.dropchop.recyclone.model.api.query.Aggregation.Wrapper.*;
import static com.dropchop.recyclone.model.api.query.Aggregation.Wrapper.sum;
import static com.dropchop.recyclone.model.api.query.Condition.*;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.*;
import static com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper.mapAggregation;
import static com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper.mapCondition;
import static com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper.elasticQueryMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.skyscreamer.jsonassert.JSONAssert;

public class ElasticsearchMapperTest {

  @Test
  public void testConditionMapper() throws JsonProcessingException, JSONException {
    Condition c = and(
      or(
        not(
          field(
            "updated",
            in(3, 2, 1)
          )
        ),
        field(
          "title",
          in(1, 2, 3)
        )
      ),
      field(
        "lol",
        eq("drone")
      )
    );

    String correctJson =
      """
        {
           "must": [
             {
               "bool": {
                 "should": [
                   {
                     "bool": {
                       "must_not":
                         {
                           "terms": {
                             "updated": [3, 2, 1]
                           }
                         }
                     }
                   },
                   {
                     "terms": {
                       "title": [1, 2, 3]
                     }
                   }
                 ],
                 "minimum_should_match": 1
               }
             },
             {
               "term": {
                 "lol": "drone"
               }
             }
           ]
         }
      """;

    QueryNodeObject ob = mapCondition(c, null);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    String json = objectMapper.writeValueAsString(ob);

    JSONAssert.assertEquals(correctJson, json, true);
  }

  @Test
  public void testAggregationMapper() throws JsonProcessingException, JSONException {
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

    String jsonOutput1 = """
      {
         "aggs": {
           "price_max": {
             "max": {
               "field": "price"
             },
             "aggs": {
               "price_histogram": {
                 "date_histogram": {
                   "field": "price",
                   "calendar_interval": "month"
                 }
               },
               "price_count": {
                 "value_count": {
                   "field": "price"
                 }
               },
               "price_avg": {
                 "avg": {
                   "field": "price"
                 },
                 "aggs": {
                   "price_cardinality": {
                     "cardinality": {
                       "field": "price"
                     },
                     "aggs": {
                       "price_terms": {
                         "terms": {
                           "field": "price"
                         }
                       }
                     }
                   }
                 }
               }
             }
           },
           "price_min": {
             "min": {
               "field": "price"
             },
             "aggs": {
               "price_histogram": {
                 "date_histogram": {
                   "field": "price",
                   "calendar_interval": "seconds"
                 },
                 "aggs": {
                   "price_sum": {
                     "sum": {
                       "field": "price"
                     }
                   }
                 }
               }
             }
           }
         }
       }
     """;

    QueryNodeObject agg = mapAggregation(a);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    String jsonOutput2 = objectMapper.writeValueAsString(agg);

    JSONAssert.assertEquals(jsonOutput1, jsonOutput2, true);
  }

  @Test
  @SuppressWarnings("unused")
  public void testQueryParamsMapper() throws JsonProcessingException {
    QueryParams params = QueryParams.builder().condition(
      and(
        or(
          field(
            "updated",
            gteLt(
              Iso8601.fromIso("2024-09-19T10:12:01.123"),
              Iso8601.fromIso("2024-09-20T11:00:01.123")
            )
          ),
          and(
            field("neki", in("one", "two", "three"))
          ),
          field("modified", Iso8601.fromIso("2024-09-19T10:12:01.123")),
          not(
            field(
              "uuid", in("6ad7cbc2-fdc3-4eb3-bb64-ba6a510004db", "c456c510-3939-4e2a-98d1-3d02c5d2c609")
            )
          )
        ),
        field("type", in(1, 2, 3)),
        field("created", Iso8601.fromIso("2024-09-19T10:12:01.123")),
        field("miki", null)
      ).and(
        field("type2", in(1, 2, 3))
      )
    ).aggregation(
      aggs(
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
      )
    ).build();

    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject correct = elasticQueryMapper(params);

    String json = ob.writeValueAsString(correct);
  }
}
