package com.dropchop.recyclone.quarkus.it.rest;

import com.dropchop.recyclone.model.api.utils.Iso8601;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.*;

import static com.dropchop.recyclone.model.api.query.Aggregation.Wrapper.*;
import static com.dropchop.recyclone.model.api.query.Condition.*;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.gteLt;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.in;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 03. 24.
 */
@Slf4j
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DummyResourceTest {

  @Inject
  ObjectMapper mapper;

  @BeforeEach
  public void setUp() {
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
        objectMapperConfig().jackson2ObjectMapperFactory((type, s) -> mapper)
    );

    try {
      RestClient restClient = RestClient.builder(new HttpHost("localhost", 9300, "http")).build();
      Request request = new Request("PUT", "dummy/_doc/1");
      request.setJsonEntity("{\n" +
        "    \"title\": \"Introduction to Java\",\n" +
        "    \"description\": \"A beginner's guide to Java programming.\",\n" +
        "    \"lang\": \"en\",\n" +
        "    \"translations\": {\n" +
        "      \"es\": \"Introducción a Java\",\n" +
        "      \"fr\": \"Introduction à Java\"\n" +
        "    },\n" +
        "    \"created\": \"2023-01-15T10:00:00Z\",\n" +
        "    \"modified\": \"2023-05-20T14:30:00Z\",\n" +
        "    \"deactivated\": null,\n" +
        "    \"code\": \"JAVA101\"\n" +
        "  }");

      restClient.performRequest(request);

      Request request2 = new Request("PUT", "dummy/_doc/2");
      request.setJsonEntity("{\n" +
        "    \"title\": \"Frontend Development with Vue.js\",\n" +
        "    \"description\": \"Comprehensive guide to building web applications using Vue.js.\",\n" +
        "    \"lang\": \"en\",\n" +
        "    \"translations\": {\n" +
        "      \"ja\": \"Vue.jsを使ったフロントエンド開発\",\n" +
        "      \"zh\": \"使用Vue.js进行前端开发\"\n" +
        "    },\n" +
        "    \"created\": \"2023-03-22T09:30:00Z\",\n" +
        "    \"modified\": \"2023-07-18T13:45:00Z\",\n" +
        "    \"deactivated\": null,\n" +
        "    \"code\": \"VUE303\"\n" +
        "  }");

      restClient.performRequest(request2);

      Request request3 = new Request("PUT", "dummy/_doc/3");
      request.setJsonEntity("{\n" +
        "    \"title\": \"Advanced Python Techniques\",\n" +
        "    \"description\": \"A deep dive into advanced Python concepts.\",\n" +
        "    \"lang\": \"en\",\n" +
        "    \"translations\": {\n" +
        "      \"de\": \"Fortgeschrittene Python-Techniken\",\n" +
        "      \"it\": \"Tecniche avanzate di Python\"\n" +
        "    },\n" +
        "    \"created\": \"2022-11-10T08:45:00Z\",\n" +
        "    \"modified\": \"2023-02-14T16:00:00Z\",\n" +
        "    \"deactivated\": \"2023-09-01T12:00:00Z\",\n" +
        "    \"code\": \"PYT202\"\n" +
        "  }");

      restClient.performRequest(request3);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  @Test
  @Order(10)
  public void dummyRestGet() {
    given()
        .log().all()
        //.accept(MediaType.APPLICATION_JSON)
        .auth().preemptive().basic("user1", "password")
        .when()
        .get("/api/public/test/dummy")
        .then()
        .statusCode(200)
        .log().all();
        //.body("[0].code", equalTo("sl")).extract().asPrettyString();
  }


  @Test
  @Order(20)
  public void dummySearch() {
    CodeParams params = CodeParams.builder().code("neki").build();
    given()
        .log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        .auth().preemptive().basic("user1", "password")
        .body(params)
        .when()
        .post("/api/public/test/dummy/search")
        .then()
        .statusCode(200)
        .log().all();
    //.body("[0].code", equalTo("sl")).extract().asPrettyString();
  }

  @Test
  @Order(30)
  public void dummyQuery() {
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
    given()
        .log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        .auth().preemptive().basic("user1", "password")
        .body(params)
        .when()
        .post("/api/public/test/dummy/query")
        .then()
        .statusCode(200)
        .log().all();
    //.body("[0].code", equalTo("sl")).extract().asPrettyString();
  }

  @Test
  @Order(30)
  public void dummyQueryTestAggregation() {
    QueryParams params = QueryParams.builder().aggregation(aggs(
      dateHistogram(
        "watch_max",
        "watch",
        "seconds",
        dateHistogram(
          "nested_watch_max",
          "watch",
          "month"
        )
      )
    )).build();
    Log.debug(params.toString());
    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(params)
      .when()
      .post("/api/public/test/dummy/query")
      .then()
      .statusCode(200)
      .log().all();
    //.body("[0].code", equalTo("sl")).extract().asPrettyString();
  }

  @Test
  @Order(30)
  public void dummyQueryTestAggregations() {
    QueryParams params = QueryParams.builder().aggregation(
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
    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(params)
      .when()
      .post("/api/public/test/dummy/query")
      .then()
      .statusCode(200)
      .log().all();
    //.body("[0].code", equalTo("sl")).extract().asPrettyString();
  }

  @Test
  @Order(30)
  public void dummyQueryCombined() {
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
    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(params)
      .when()
      .post("/api/public/test/dummy/query")
      .then()
      .statusCode(200)
      .log().all();
    //.body("[0].code", equalTo("sl")).extract().asPrettyString();
  }

  @Test
  @Order(30)
  public void dummySearchTest() {
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
    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(params)
      .when()
      .post("/api/public/test/dummy/es_search")
      .then()
      .statusCode(200)
      .log().all();
    //.body("[0].code", equalTo("sl")).extract().asPrettyString();
  }

  @Test
  @Order(30)
  public void dummySearchNullTest() {
    QueryParams params = QueryParams.builder().condition(
      field("languages.name", "si")
    ).build();
    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(params)
      .when()
      .post("/api/public/test/dummy/es_search")
      .then()
      .statusCode(200)
      .log().all();
    //.body("[0].code", equalTo("sl")).extract().asPrettyString();
  }

  @Test
  @Order(30)
  public void dummySearchOneDummyTest() {
    QueryParams params = QueryParams.builder().condition(
      and(
        field("code.keyword", "JAVA101"),
        field("lang", "en")
      )
    ).build();
    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(params)
      .when()
      .post("/api/public/test/dummy/es_search")
      .then()
      .statusCode(200)
      .log().all();
    //.body("[0].code", equalTo("sl")).extract().asPrettyString();
  }
}
