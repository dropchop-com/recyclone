package com.dropchop.recyclone.repo.es.mapper;

import com.dropchop.recyclone.model.api.query.Condition;
import com.dropchop.recyclone.model.api.query.aggregation.AggregationList;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONException;
import org.junit.Test;

import static com.dropchop.recyclone.model.api.query.Aggregation.Wrapper.*;
import static com.dropchop.recyclone.model.api.query.Aggregation.Wrapper.sum;
import static com.dropchop.recyclone.model.api.query.Condition.*;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.eq;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.in;
import static com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper.mapAggregation;
import static com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper.mapCondition;

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
}
