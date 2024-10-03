package com.dropchop.recyclone.quarkus.it.rest;

import com.dropchop.recyclone.model.api.query.Aggregation;
import com.dropchop.recyclone.model.api.query.AggregationImpl;
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
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dropchop.recyclone.model.api.query.Aggregation.*;
import static com.dropchop.recyclone.model.api.query.Aggregation.aggregationField;
import static com.dropchop.recyclone.model.api.query.Condition.*;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.gteLt;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.in;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 03. 24.
 */
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
    QueryParams params = QueryParams.builder().aggregation(List.of(
      dateHistogram(
        aggregationField("watch_max", "watch"),
        dateHistogram(
          aggregationField("nested_watch_max", "watch")
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
      List.of(
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
            ),
            aggregationField("watch_max", "watch")
          )
        ),
        terms(
          aggregationField("nested_worker_terms", "worker")
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
      List.of(
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
}
