package com.dropchop.recyclone.base.es.repo.mapper;

import com.dropchop.recyclone.base.api.model.query.knn.KnnQuery;
import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;
import com.dropchop.recyclone.base.es.repo.query.DefaultElasticQueryBuilder;
import com.dropchop.recyclone.base.es.repo.query.ElasticQueryBuilder.ValidationData;
import com.dropchop.recyclone.base.jackson.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.List;

import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.*;
import static com.dropchop.recyclone.base.api.model.query.Condition.*;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.*;

public class ElasticsearchQueryBuilderTest {

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
  public void testQueryParams() throws JsonProcessingException, JSONException {
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
    ).aggregate(
      aggs(
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
          min(
            "neste_min",
            "worker"
          )
        )
      )
    ).build();

    String expectedJson = """
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
                }
            },
            "nested_nested_worker_cardinality": {
                "cardinality": {
                    "field": "worker"
                }
            },
            "nested_nested_worker_dateHistogram": {
                "date_histogram": {
                    "calendar_interval": "month",
                    "field": "worker"
                }
            },
            "nested_worker_terms": {
                "aggs": {
                    "neste_min": {
                        "min": {
                            "field": "worker"
                        }
                    }
                },
                "terms": {
                    "field": "worker"
                }
            }
        },
        from: 0,
        size: 100
      }
      """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();

    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject correct = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(correct);

    JSONAssert.assertEquals(expectedJson, json, true);
  }

  @Test
  @SuppressWarnings("unused")
  public void debugConditionMustNotExist() {
    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();

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

    QueryNodeObject expected = es.build(new ValidationData(), params);
  }

  @Test
  public void processAdvancedText() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder().condition(
      and(
        advancedText("text", "\"krem* proti gubam\"", null, 1, false)
      )
    ).build();

    String correctJson =
      """
        {
          "query": {
            "bool": {
              "must": {
                "span_near": {
                  "in_order": false,
                  "clauses": [
                    {
                      "span_multi": {
                        "match": {
                          "prefix": {
                            "text": {
                              "value": "krem"
                            }
                          }
                        }
                      }
                    },
                    {
                      "span_term": {
                        "text": {
                          "value": "proti"
                        }
                      }
                    },
                    {
                      "span_term": {
                        "text": {
                          "value": "gubam"
                        }
                      }
                    }
                  ],
                  "slop": 1
                }
              }
            }
          },
          from: 0,
          size: 100
        }
        """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject correct = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(correct);
    JSONAssert.assertEquals(correctJson, json, true);
  }

  @Test
  public void processAdvancedSearchTestWithWildcard() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder().condition(
      and(
        advancedText("text", "\"kr*m* pr*ti gubam\"", true)
      )
    ).build();

    String correctJson =
      """
        {
          "query" : {
            "bool" : {
              "must" : {
                "span_near" : {
                  "clauses" : [ {
                    "span_multi" : {
                      "match" : {
                        "wildcard" : {
                          "case_insensitive": true,
                          "text" : {
                            "value" : "kr*m*"
                          }
                        }
                      }
                    }
                  }, {
                    "span_multi" : {
                      "match" : {
                        "wildcard" : {
                          "case_insensitive": true,
                          "text" : {
                            "value" : "pr*ti"
                          }
                        }
                      }
                    }
                  }, {
                    "span_term" : {
                      "case_insensitive": true,
                      "text" : {
                        "value" : "gubam"
                      }
                    }
                  } ],
                  "in_order" : true,
                  "slop" : 0
                }
              }
            }
          },
          from: 0,
          size: 100
        }
        """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject correct = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(correct);
    JSONAssert.assertEquals(correctJson, json, true);
  }

  @Test
  public void processAdvancedTextAutoCase() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder().condition(
      and(
        advancedText("text", "\"Nivea krem*\"")
      )
    ).build();

    String correctJson =
      """
        {
          "query": {
            "bool": {
              "must": {
                "span_near": {
                  "in_order": true,
                  "clauses": [
                    {
                      "span_term": {
                        "case_insensitive": false,
                        "text": {
                          "value": "Nivea"
                        }
                      }
                    },
                    {
                      "span_multi": {
                        "match": {
                          "prefix": {
                            "text": {
                              "value": "krem"
                            }
                          }
                        }
                      }
                    }
                  ],
                  "slop": 0
                }
              }
            }
          },
          from: 0,
          size: 100
        }
        """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject correct = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(correct);
    JSONAssert.assertEquals(correctJson, json, true);
  }

  @Test
  public void processElasticIncludeCase() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder().aggregate(
      aggs(
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
      )
    ).build();

    String correctJson = """
       {
         "query" : {
           "match_all" : { }
         },
         "aggs" : {
           "price_histogram" : {
             "date_histogram" : {
               "field" : "price",
               "calendar_interval" : "seconds"
             },
             "aggs" : {
               "price_sum" : {
                 "terms" : {
                   "field" : "price",
                   "include" : [ "include_ports" ]
                 }
               }
             }
           }
         },
         from: 0,
         size: 100
       }
       """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject correct = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(correct);
    JSONAssert.assertEquals(correctJson, json, true);
  }

  @Test
  public void processElasticExcludeCase() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder().aggregate(
      aggs(
        dateHistogram(
          "price_histogram",
          "price",
          "seconds",
          terms(
            "price_sum",
            "price",
            filter(
              excludes(List.of("include_ports"))
            )
          )
        )
      )
    ).build();

    String correctJson = """
      {
        "query" : {
          "match_all" : { }
        },
        "aggs" : {
          "price_histogram" : {
            "date_histogram" : {
              "field" : "price",
              "calendar_interval" : "seconds"
            },
            "aggs" : {
              "price_sum" : {
                "terms" : {
                  "field" : "price",
                  "exclude" : [ "include_ports" ]
                }
              }
            }
          }
        },
        from: 0,
        size: 100
      }
      """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject correct = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(correct);
    JSONAssert.assertEquals(correctJson, json, true);
  }

  @Test
  public void processElasticIncludeExcludeCase() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder().aggregate(
      aggs(
        dateHistogram(
          "price_histogram",
          "price",
          "seconds",
          terms(
            "price_sum",
            "price",
            filter(
              includes(List.of("include_ports")),
              excludes(List.of("exclude_ports"))
            )
          )
        )
      )
    ).build();

    String correctJson = """
      {
        "query" : {
          "match_all" : { }
        },
        "aggs" : {
          "price_histogram" : {
            "date_histogram" : {
              "field" : "price",
              "calendar_interval" : "seconds"
            },
            "aggs" : {
              "price_sum" : {
                "terms" : {
                  "field" : "price",
                  "include" : [ "include_ports" ],
                  "exclude": [ "exclude_ports" ]
                }
              }
            }
          }
        },
        from: 0,
        size: 100
      }
      """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject correct = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(correct);
    JSONAssert.assertEquals(correctJson, json, true);
  }

  @Test
  public void processNoFilterElasticCase() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder().aggregate(
      aggs(
        dateHistogram(
          "price_histogram",
          "price",
          "seconds",
          terms(
            "price_sum",
            "price"
          )
        )
      )
    ).build();

    String correctJson = """
      {
        "query" : {
          "match_all" : { }
        },
        "aggs" : {
          "price_histogram" : {
            "date_histogram" : {
              "field" : "price",
              "calendar_interval" : "seconds"
            },
            "aggs" : {
              "price_sum" : {
                "terms" : {
                  "field" : "price"
                }
              }
            }
          }
        },
        from: 0,
        size: 100
      }
      """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject correct = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(correct);
    JSONAssert.assertEquals(correctJson, json, true);
  }

  @Test
  public void testSimpleKnnQuery() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder()
      .knnQuery(KnnQuery.builder()
        .field("product_embedding")
        .queryVector(new float[]{0.1f, 0.2f, 0.3f, 0.4f, 0.5f})
        .k(10)
        .build())
      .build();

    String expectedJson = """
      {
        "knn": {
          "field": "product_embedding",
          "query_vector": [0.1, 0.2, 0.3, 0.4, 0.5],
          "k": 10
        },
        "query": {
          "match_all": {}
        },
        "from": 0,
        "size": 100
      }
      """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject result = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(result);
    JSONAssert.assertEquals(expectedJson, json, true);
  }

  @Test
  public void testHybridSearchKnnWithTextConditions() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder()
      .condition(and(
        field("title", "smartphone"),
        field("category", eq("electronics"))
      ))
      .knnQuery(KnnQuery.builder()
        .field("product_embedding")
        .queryVector(new float[]{0.5f, 0.3f, 0.8f})
        .k(5)
        .boost(1.5f)
        .build())
      .build();

    String expectedJson = """
      {
        "query": {
          "bool": {
            "must": [
              {
                "bool": {
                  "must": [
                    {
                      "term": {
                        "title": "smartphone"
                      }
                    },
                    {
                      "term": {
                        "category": "electronics"
                      }
                    }
                  ]
                }
              }
            ],
            "should": [
              {
                "knn": {
                  "field": "product_embedding",
                  "query_vector": [0.5, 0.3, 0.8],
                  "k": 5,
                  "boost": 1.5
                }
              }
            ]
          }
        },
        "from": 0,
        "size": 100
      }
      """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject result = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(result);
    JSONAssert.assertEquals(expectedJson, json, true);
  }

  @Test
  public void testKnnQueryWithComplexFilter() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder()
      .knnQuery(KnnQuery.builder()
        .field("article_embedding")
        .queryVector(new float[]{0.2f, 0.4f, 0.6f, 0.8f})
        .k(12)
        .numCandidates(60)
        .filter(or(
          and(
            field("status", eq("published")),
            field("views", gte(1000))
          ),
          field("featured", eq(true))
        ))
        .similarity(0.7f)
        .name("article_similarity_search")
        .build())
      .build();

    String expectedJson = """
      {
        "knn": {
          "field": "article_embedding",
          "query_vector": [0.2, 0.4, 0.6, 0.8],
          "k": 12,
          "num_candidates": 60,
          "filter": {
            "bool": {
              "should": [
                {
                  "bool": {
                    "must": [
                      {
                        "term": {
                          "status": "published"
                        }
                      },
                      {
                        "range": {
                          "views": {
                            "gte": 1000
                          }
                        }
                      }
                    ]
                  }
                },
                {
                  "term": {
                    "featured": true
                  }
                }
              ],
              "minimum_should_match": 1
            }
          },
          "similarity": 0.7,
          "_name": "article_similarity_search"
        },
        "query": {
          "match_all": {}
        },
        "from": 0,
        "size": 100
      }
      """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject result = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(result);
    JSONAssert.assertEquals(expectedJson, json, true);
  }

  @Test
  public void testKnnQueryWithAggregations() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder()
      .knnQuery(KnnQuery.builder()
        .field("user_behavior_embedding")
        .queryVector(new float[]{0.3f, 0.7f, 0.9f})
        .k(20)
        .build())
      .aggregate(aggs(
        terms("category_breakdown", "category", 10),
        avg("avg_price", "price"),
        cardinality("unique_brands", "brand")
      ))
      .build();

    String expectedJson = """
      {
        "knn": {
          "field": "user_behavior_embedding",
          "query_vector": [0.3, 0.7, 0.9],
          "k": 20
        },
        "query": {
          "match_all": {}
        },
        "aggs": {
          "category_breakdown": {
            "terms": {
              "field": "category",
              "size": 10
            }
          },
          "avg_price": {
            "avg": {
              "field": "price"
            }
          },
          "unique_brands": {
            "cardinality": {
              "field": "brand"
            }
          }
        },
        "from": 0,
        "size": 100
      }
      """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject result = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(result);
    JSONAssert.assertEquals(expectedJson, json, true);
  }

  @Test
  public void testComplexHybridSearchWithAggregations() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder()
      .condition(and(
        or(
          field("category", in("electronics", "gadgets")),
          field("brand", eq("Apple"))
        ),
        field("price", gteLt(50.0, 1000.0)),
        not(field("discontinued", eq(true)))
      ))
      .knnQuery(KnnQuery.builder()
        .field("product_features")
        .queryVector(new float[]{0.4f, 0.6f, 0.2f, 0.8f})
        .k(10)
        .numCandidates(100)
        .boost(2.0f)
        .name("feature_similarity")
        .build())
      .aggregate(aggs(
        terms("top_categories", "category", 5),
        dateHistogram("sales_over_time", "created", "month")
      ))
      .build();

    String expectedJson = """
      {
        "query": {
          "bool": {
            "must": [
              {
                "bool": {
                  "must": [
                    {
                      "bool": {
                        "should": [
                          {
                            "terms": {
                              "category": ["electronics", "gadgets"]
                            }
                          },
                          {
                            "term": {
                              "brand": "Apple"
                            }
                          }
                        ],
                        "minimum_should_match": 1
                      }
                    },
                    {
                      "range": {
                        "price": {
                          "gte": 50.0,
                          "lt": 1000.0
                        }
                      }
                    },
                    {
                      "bool": {
                        "must_not": {
                          "term": {
                            "discontinued": true
                          }
                        }
                      }
                    }
                  ]
                }
              }
            ],
            "should": [
              {
                "knn": {
                  "field": "product_features",
                  "query_vector": [0.4, 0.6, 0.2, 0.8],
                  "k": 10,
                  "num_candidates": 100,
                  "boost": 2.0,
                  "_name": "feature_similarity"
                }
              }
            ]
          }
        },
        "aggs": {
          "top_categories": {
            "terms": {
              "field": "category",
              "size": 5
            }
          },
          "sales_over_time": {
            "date_histogram": {
              "field": "created",
              "calendar_interval": "month"
            }
          }
        },
        "from": 0,
        "size": 100
      }
      """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    QueryNodeObject result = es.build(new ValidationData(), params);

    String json = ob.writeValueAsString(result);
    JSONAssert.assertEquals(expectedJson, json, true);
  }

}
