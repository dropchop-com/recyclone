package com.dropchop.recyclone.base.es.repo.mapper;

import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.jackson.ObjectMapperFactory;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.*;
import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.sum;
import static com.dropchop.recyclone.base.api.model.query.Condition.*;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.skyscreamer.jsonassert.JSONAssert;

public class ElasticsearchMapperTest {

  /*@Test
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
  }*/

  @Test
  @SuppressWarnings("unused")
  public void testQueryParamsMapper() throws JsonProcessingException, JSONException {
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
            field("neki", in("one", "two", "three")),
            field("created", lt(Iso8601.fromIso("2024-09-19T10:12:01.123")))
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
        field("type2", in(1, 2, 3)),
        field("type4", "type8")
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

    String correctJson = """
    
      {
           "query": {
             "bool": {
               "must": [
                 {
                   "bool": {
                     "should": [
                       {
                         "range": {
                           "updated": {
                             "gte": "2024-09-19T10:12:01.123+02",
                             "lt": "2024-09-20T11:00:01.123+02"
                           }
                         }
                       },
                       {
                         "bool": {
                           "must": [
                             {
                               "terms": {
                                 "neki": [
                                   "one",
                                   "two",
                                   "three"
                                 ]
                               }
                             },
                             {
                               "range": {
                                 "created": {
                                   "lt": "2024-09-19T10:12:01.123+02"
                                 }
                               }
                             }
                           ]
                         }
                       },
                       {
                         "range": {
                           "modified": {
                             "gte": "2024-09-19T10:12:01.123+02",
                             "lte": "2024-09-19T10:12:01.123+02"
                           }
                         }
                       },
                       {
                         "bool": {
                           "must_not": {
                             "terms": {
                               "uuid": [
                                 "6ad7cbc2-fdc3-4eb3-bb64-ba6a510004db",
                                 "c456c510-3939-4e2a-98d1-3d02c5d2c609"
                               ]
                             }
                           }
                         }
                       }
                     ],
                     "minimum_should_match": 1
                   }
                 },
                 {
                   "terms": {
                     "type": [
                       1,
                       2,
                       3
                     ]
                   }
                 },
                 {
                   "range": {
                     "created": {
                       "gte": "2024-09-19T10:12:01.123+02",
                       "lte": "2024-09-19T10:12:01.123+02"
                     }
                   }
                 },
                 {
                   "bool": {
                     "must_not": {
                       "exists": {
                         "field": "miki"
                       }
                     }
                   }
                 },
                 {
                   "terms": {
                     "type2": [
                       1,
                       2,
                       3
                     ]
                   }
                 },
                 {
                   "term": {
                     "type4": "type8"
                   }
                 }
               ]
             }
           },
           "aggs": {
             "watch_max": {
               "max": {
                 "field": "watch"
               },
               "aggs": {
                 "nested_worker_sum": {
                   "sum": {
                     "field": "worker"
                   }
                 },
                 "nested_worker_min": {
                   "min": {
                     "field": "worker"
                   }
                 },
                 "nested_worker_avg": {
                   "avg": {
                     "field": "worker"
                   }
                 },
                 "nested_nested_worker_count": {
                   "value_count": {
                     "field": "worker"
                   }
                 }
               }
             },
             "nested_nested_worker_cardinality": {
               "cardinality": {
                 "field": "worker"
               }
             },
             "nested_nested_worker_dateHistogram": {
               "date_histogram": {
                 "field": "worker",
                 "calendar_interval": "month"
               }
             },
             "nested_worker_terms": {
               "terms": {
                 "field": "worker"
               }
             }
           }
         }
    """;

    ElasticQueryMapper es = new ElasticQueryMapper();

    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject correct = es.mapToString(params);

    String json = ob.writeValueAsString(correct);

    JSONAssert.assertEquals(correctJson, json, true);
  }

  @Test
  @SuppressWarnings("unused")
  public void debugConditionMustNotExist() {
    ElasticQueryMapper es = new ElasticQueryMapper();

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
    ).build();

    QueryNodeObject correct = es.mapToString(params);
  }
}
