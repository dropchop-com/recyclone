package com.dropchop.recyclone.quarkus.it.rest;

import com.dropchop.recyclone.model.api.utils.Iso8601;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.ZonedDateTime;
import java.util.List;

import static com.dropchop.recyclone.model.api.query.Aggregation.Wrapper.*;
import static com.dropchop.recyclone.model.api.query.Condition.*;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.gteLt;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.in;
import static io.restassured.RestAssured.given;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 03. 24.
 */
@Slf4j
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DummyResourceTest {

  @Test
  @Order(5)
  public void create() {
    Dummy dummy1 = new Dummy();
    dummy1.setTitle("Introduction to Java");
    dummy1.setDescription("A comprehensive guide to Java programming.");
    dummy1.setLang("en");
    dummy1.setCreated(ZonedDateTime.now().minusDays(10));
    dummy1.setModified(ZonedDateTime.now());
    dummy1.setDeactivated(null);
    dummy1.setCode("dummy_code1");

    Dummy dummy2 = new Dummy();
    dummy2.setTitle("Advanced Python Techniques");
    dummy2.setDescription("Explore advanced concepts in Python programming.");
    dummy2.setLang("en");
    dummy2.setCreated(ZonedDateTime.now().minusDays(20));
    dummy2.setModified(ZonedDateTime.now().minusDays(5));
    dummy2.setDeactivated(null);
    dummy2.setCode("dummy_code2");

    Dummy dummy3 = new Dummy();
    dummy3.setTitle("Introduction to Machine Learning");
    dummy3.setDescription("An introductory course to machine learning and its applications.");
    dummy3.setLang("en");
    dummy3.setCreated(ZonedDateTime.now().minusMonths(2));
    dummy3.setModified(ZonedDateTime.now().minusDays(10));
    dummy3.setDeactivated(null);
    dummy3.setCode("dummy_code3");

    List<Dummy> dummies = List.of(dummy1, dummy2, dummy3);

    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("editor1", "password")
      .body(dummies)
      .when()
      .post("/api/internal/test/dummy")
      .then()
      .statusCode(200)
      .log().all();
  }

  @Test
  @Order(10)
  public void dummyRestGet() {
    given()
      .log().all()
      //.accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("editor1", "password")
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
    CodeParams params = CodeParams.builder().code("dummy_code1").build();
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
  public void deleteById() {
    CodeParams params = CodeParams.builder().code("dummy_code1").build();
    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("editor1", "password")
      .body(params)
      .when()
      .delete("/api/internal/test/dummy/deleteById")
      .then()
      .statusCode(200)
      .log().all();
  }
  /*
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
  */

  @Test
  @Order(30)
  public void dummyQueryTest() {
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
      .post("/api/public/test/dummy/query")
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
      .post("/api/public/test/dummy/query")
      .then()
      .statusCode(200)
      .log().all();
    //.body("[0].code", equalTo("sl")).extract().asPrettyString();
  }

  @Test
  @Order(30)
  public void dummySearchMultipleDummyTest() {
    QueryParams params = QueryParams.builder().condition(
      field("lang", "en")
    ).build();
    params.tryGetResultFilter().setSize(1);
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
  public void delete() {
    Dummy dummy1 = new Dummy();
    dummy1.setTitle("Introduction to Java");
    dummy1.setDescription("A comprehensive guide to Java programming.");
    dummy1.setLang("en");
    dummy1.setCreated(ZonedDateTime.now().minusDays(10));
    dummy1.setModified(ZonedDateTime.now());
    dummy1.setDeactivated(null);
    dummy1.setCode("dummy_delete_code1");

    Dummy dummy2 = new Dummy();
    dummy2.setTitle("Advanced Python Techniques");
    dummy2.setDescription("Explore advanced concepts in Python programming.");
    dummy2.setLang("en");
    dummy2.setCreated(ZonedDateTime.now().minusDays(20));
    dummy2.setModified(ZonedDateTime.now().minusDays(5));
    dummy2.setDeactivated(null);
    dummy2.setCode("dummy_delete_code2");

    Dummy dummy3 = new Dummy();
    dummy3.setTitle("Introduction to Machine Learning");
    dummy3.setDescription("An introductory course to machine learning and its applications.");
    dummy3.setLang("es");
    dummy3.setCreated(ZonedDateTime.now().minusMonths(2));
    dummy3.setModified(ZonedDateTime.now().minusDays(10));
    dummy3.setDeactivated(null);
    dummy3.setCode("dummy_delete_code3");

    List<Dummy> dummies = List.of(dummy1, dummy2, dummy3);

    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("editor1", "password")
      .body(dummies)
      .when()
      .post("/api/internal/test/dummy")
      .then()
      .statusCode(200)
      .log().all();

    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("editor1", "password")
      .body(dummies)
      .when()
      .delete("/api/internal/test/dummy")
      .then()
      .statusCode(200)
      .log().all();
  }
}