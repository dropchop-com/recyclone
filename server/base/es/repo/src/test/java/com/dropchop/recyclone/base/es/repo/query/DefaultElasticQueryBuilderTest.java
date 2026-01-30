package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.query.aggregation.Sort;
import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;
import com.dropchop.recyclone.base.jackson.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.List;

import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.*;
import static com.dropchop.recyclone.base.api.model.query.Aggregation.topHits;
import static com.dropchop.recyclone.base.api.model.query.Condition.*;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 01. 2026.
 */
@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
class DefaultElasticQueryBuilderTest {

  @Test
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
      "query" : {
        "bool" : {
          "must" : [ {
            "bool" : {
              "should" : [ {
                "range" : {
                  "updated" : {
                    "gte" : "2024-09-19T10:12:01.123+02",
                    "lt" : "2024-09-20T11:00:01.123+02"
                  }
                }
              }, {
                "bool" : {
                  "must" : [ {
                    "terms" : {
                      "neki" : [ "one", "two", "three" ]
                    }
                  }, {
                    "range" : {
                      "created" : {
                        "lt" : "2024-09-19T10:12:01.123+02"
                      }
                    }
                  } ]
                }
              }, {
                "term" : {
                  "modified" : "2024-09-19T10:12:01.123+02"
                }
              }, {
                "bool" : {
                  "must_not" : {
                    "terms" : {
                      "uuid" : [ "6ad7cbc2-fdc3-4eb3-bb64-ba6a510004db", "c456c510-3939-4e2a-98d1-3d02c5d2c609" ]
                    }
                  }
                }
              } ],
              "minimum_should_match" : 1
            }
          }, {
            "terms" : {
              "type" : [ 1, 2, 3 ]
            }
          }, {
            "term" : {
              "created" : "2024-09-19T10:12:01.123+02"
            }
          }, {
            "terms" : {
              "type2" : [ 1, 2, 3 ]
            }
          }, {
            "term" : {
              "type4" : "type8"
            }
          } ],
          "must_not" : {
            "exists" : {
              "field" : "miki"
            }
          }
        }
      },
      "aggs" : {
        "watch_max" : {
          "max" : {
            "field" : "watch"
          }
        },
        "nested_nested_worker_cardinality" : {
          "cardinality" : {
            "field" : "worker"
          }
        },
        "nested_nested_worker_dateHistogram" : {
          "date_histogram" : {
            "field" : "worker",
            "calendar_interval" : "month"
          }
        },
        "nested_worker_terms" : {
          "terms" : {
            "field" : "worker"
          },
          "aggs" : {
            "neste_min" : {
              "min" : {
                "field" : "worker"
              }
            }
          }
        }
      },
      "from" : 0,
      "size" : 100
    }
    """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();

    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    IQueryObject correct = es.build(params);

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

    IQueryObject expected = es.build(params);
  }

  @Test
  public void processAdvancedText() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder().condition(
        and(
            advancedText("text", "\"krem* proti gubam\"", null, 1, false)
        )
    ).build();

    String correctJson = """
    {
      "query" : {
        "bool" : {
          "must" : {
            "bool" : {
              "should" : {
                "span_near" : {
                  "clauses" : [ {
                    "span_multi" : {
                      "match" : {
                        "prefix" : {
                          "text" : {
                            "value" : "krem"
                          }
                        }
                      }
                    }
                  }, {
                    "span_term" : {
                      "text" : {
                        "value" : "proti"
                      }
                    }
                  }, {
                    "span_term" : {
                      "text" : {
                        "value" : "gubam"
                      }
                    }
                  } ],
                  "in_order" : false,
                  "slop" : 1
                }
              },
              "minimum_should_match" : 1
            }
          }
        }
      },
      "from" : 0,
      "size" : 100
    }
    """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    IQueryObject correct = es.build(params);

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

    String correctJson = """
    {
      "query" : {
        "bool" : {
          "must" : {
            "bool" : {
              "should" : {
                "span_near" : {
                  "clauses" : [ {
                    "span_multi" : {
                      "match" : {
                        "wildcard" : {
                          "text" : {
                            "value" : "kr*m*",
                            "case_insensitive" : true
                          }
                        }
                      }
                    }
                  }, {
                    "span_multi" : {
                      "match" : {
                        "wildcard" : {
                          "text" : {
                            "value" : "pr*ti",
                            "case_insensitive" : true
                          }
                        }
                      }
                    }
                  }, {
                    "span_term" : {
                      "text" : {
                        "value" : "gubam"
                      }
                    }
                  } ]
                }
              },
              "minimum_should_match" : 1
            }
          }
        }
      },
      "from" : 0,
      "size" : 100
    }
    """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    IQueryObject correct = es.build(params);

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

    String correctJson = """
    {
      "query" : {
        "bool" : {
          "must" : {
            "bool" : {
              "should" : {
                "span_near" : {
                  "clauses" : [ {
                    "span_term" : {
                      "text" : {
                        "value" : "nivea"
                      }
                    }
                  }, {
                    "span_multi" : {
                      "match" : {
                        "prefix" : {
                          "text" : {
                            "value" : "krem"
                          }
                        }
                      }
                    }
                  } ]
                }
              },
              "minimum_should_match" : 1
            }
          }
        }
      },
      "from" : 0,
      "size" : 100
    }
    """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    IQueryObject correct = es.build(params);

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
                        includes("include_ports")
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
     "from": 0,
     "size": 100
    }
    """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    IQueryObject correct = es.build(params);

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
                        excludes(List.of("exclude_ports_1", "exclude_ports_2"))
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
                "exclude" : [ "exclude_ports_1", "exclude_ports_2" ]
              }
            }
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
    IQueryObject correct = es.build(params);

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
                        includes("include_ports.*"), // reg ex
                        excludes("exclude_ports.*")  // reg ex
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
                "include" : "include_ports.*",
                "exclude": "exclude_ports.*"
              }
            }
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
    IQueryObject correct = es.build(params);

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
      "from": 0,
      "size": 100
    }
    """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    IQueryObject correct = es.build(params);

    String json = ob.writeValueAsString(correct);
    JSONAssert.assertEquals(correctJson, json, true);
  }

  @Test
  public void testHybridSearchKnnWithTextConditions() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder()
        .condition(and(
            field("title", "smartphone"),
            field("category", eq("electronics")),
            knn("product_embedding", new float[]{0.5f, 0.3f, 0.8f}, 5)
        ))
        .build();

    String expectedJson = """
    {
     "from" : 0,
     "size" : 100,
     "query" : {
       "bool" : {
         "must" : [ {
           "term" : {
             "title" : "smartphone"
           }
         }, {
           "term" : {
             "category" : "electronics"
           }
         }, {
           "knn" : {
             "field" : "product_embedding",
             "query_vector" : [ 0.5, 0.3, 0.8 ],
             "k" : 5
           }
         } ]
       }
     }
    }
    """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    IQueryObject result = es.build(params);

    String json = ob.writeValueAsString(result);
    JSONAssert.assertEquals(expectedJson, json, true);
  }

  @Test
  public void testKnnQueryWithComplexFilter() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder()
        .condition(
            knn(
                "article_embedding",
                new float[]{0.2f, 0.4f, 0.6f, 0.8f},
                12,
                0.7f,
                or(
                    and(
                        field("status", eq("published")),
                        field("views", gte(1000))
                    ),
                    field("featured", eq(true))
                )
            )
        )
        .build();

    String expectedJson = """
    {
      "from" : 0,
      "size" : 100,
      "query" : {
        "bool" : {
          "must" : {
            "knn" : {
              "field" : "article_embedding",
              "query_vector" : [ 0.2, 0.4, 0.6, 0.8 ],
              "k" : 12,
              "similarity" : 0.7,
              "filter" : {
                "bool" : {
                  "should" : [ {
                    "bool" : {
                      "must" : [ {
                        "term" : {
                          "status" : "published"
                        }
                      }, {
                        "range" : {
                          "views" : {
                            "gte" : 1000
                          }
                        }
                      } ]
                    }
                  }, {
                    "term" : {
                      "featured" : true
                    }
                  } ],
                  "minimum_should_match" : 1
                }
              }
            }
          }
        }
      }
    }
    """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    IQueryObject result = es.build(params);

    String json = ob.writeValueAsString(result);
    JSONAssert.assertEquals(expectedJson, json, true);
  }

  @Test
  public void testKnnQueryWithAggregations() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder()
        .condition(
            knn("user_behavior_embedding", new float[]{0.3f, 0.7f, 0.9f}, 20)
        )
        .aggregate(aggs(
            terms("category_breakdown", "category", 10),
            avg("avg_price", "price"),
            cardinality("unique_brands", "brand")
        ))
        .build();

    String expectedJson = """
    {
      "from" : 0,
      "size" : 100,
      "query" : {
        "bool" : {
          "must" : {
            "knn" : {
              "field" : "user_behavior_embedding",
              "query_vector" : [ 0.3, 0.7, 0.9 ],
              "k" : 20
            }
          }
        }
      },
      "aggs" : {
        "category_breakdown" : {
          "terms" : {
            "field" : "category",
            "size" : 10
          }
        },
        "avg_price" : {
          "avg" : {
            "field" : "price"
          }
        },
        "unique_brands" : {
          "cardinality" : {
            "field" : "brand"
          }
        }
      }
    }
    """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    IQueryObject result = es.build(params);

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
            not(field("discontinued", eq(true))),
            knn("product_features", new float[]{0.4f, 0.6f, 0.2f, 0.8f}, 10)
        ))
        .aggregate(aggs(
            terms("top_categories", "category", 5),
            dateHistogram("sales_over_time", "created", "month")
        ))
        .build();

    String expectedJson = """
    {
      "from" : 0,
      "size" : 100,
      "query" : {
        "bool" : {
          "must" : [ {
            "bool" : {
              "should" : [ {
                "terms" : {
                  "category" : [ "electronics", "gadgets" ]
                }
              }, {
                "term" : {
                  "brand" : "Apple"
                }
              } ],
              "minimum_should_match" : 1
            }
          }, {
            "range" : {
              "price" : {
                "gte" : 50.0,
                "lt" : 1000.0
              }
            }
          }, {
            "bool" : {
              "must_not" : {
                "term" : {
                  "discontinued" : true
                }
              }
            }
          }, {
            "knn" : {
              "field" : "product_features",
              "query_vector" : [ 0.4, 0.6, 0.2, 0.8 ],
              "k" : 10
            }
          } ]
        }
      },
      "aggs" : {
        "top_categories" : {
          "terms" : {
            "field" : "category",
            "size" : 5
          }
        },
        "sales_over_time" : {
          "date_histogram" : {
            "field" : "created",
            "calendar_interval" : "month"
          }
        }
      }
    }
    """;

    DefaultElasticQueryBuilder es = new DefaultElasticQueryBuilder();
    ObjectMapperFactory factory = new ObjectMapperFactory();
    ObjectMapper ob = factory.createObjectMapper();
    IQueryObject result = es.build(params);

    String json = ob.writeValueAsString(result);
    JSONAssert.assertEquals(expectedJson, json, true);
  }

  @Test
  public void testKnnAsCondition() throws JsonProcessingException, JSONException {
    QueryParams params = QueryParams.builder()
        .condition(and(
            field("category", eq("electronics")),
            knn("product_embedding", new float[]{0.1f, 0.2f, 0.3f}, 5)
        ))
        .build();

    String expectedJson = """
    {
      "query": {
        "bool": {
          "must": [
            {
              "term": {
                "category": "electronics"
              }
            },
            {
              "knn": {
                "field": "product_embedding",
                "query_vector": [0.1, 0.2, 0.3],
                "k": 5
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
    IQueryObject result = es.build(params);

    String json = ob.writeValueAsString(result);
    JSONAssert.assertEquals(expectedJson, json, true);
  }

  @Test
  public void testTopHitsAggregation() throws JsonProcessingException, JSONException {
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
                        includes("include_ports")
                    ),
                    topHits(
                        "NewsSegmentHits",
                        50,
                        List.of(new Sort("clickCount", "desc")))
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
             },
             "aggs": {
               "NewsSegmentHits": {
                 "top_hits": {
                   "size": 50,
                   "sort": [
                     {
                       "clickCount": {
                         "order": "desc"
                       }
                     }
                   ]
                 }
               }
             }
           }
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
    IQueryObject correct = es.build(params);

    String json = ob.writeValueAsString(correct);
    JSONAssert.assertEquals(correctJson, json, true);
  }

  @Test
  public void testTopHitsNumericTypeAggregation() throws JsonProcessingException, JSONException {
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
                        includes("include_ports")
                    ),
                    topHits(
                        "NewsSegmentHits",
                        50,
                        List.of(new Sort("clickCount", "desc", "long")))
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
             },
             "aggs": {
               "NewsSegmentHits": {
                 "top_hits": {
                   "size": 50,
                   "sort": [
                     {
                       "clickCount": {
                         "order": "desc",
                         "numeric_type": "long"
                       }
                     }
                   ]
                 }
               }
             }
           }
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
    IQueryObject correct = es.build(params);

    String json = ob.writeValueAsString(correct);
    JSONAssert.assertEquals(correctJson, json, true);
  }

}