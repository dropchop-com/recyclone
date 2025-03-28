package com.dropchop.recyclone.quarkus.it.rest.dummy;

import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.dropchop.recyclone.base.dto.model.base.DtoCode;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.rest.dummy.mock.DummyMockData;
import com.dropchop.recyclone.quarkus.runtime.elasticsearch.ElasticSearchTestHelper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.*;
import static com.dropchop.recyclone.base.api.model.query.Condition.*;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.gteLt;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.in;
import static com.dropchop.recyclone.base.api.model.rest.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 03. 24.
 */
@Slf4j
@QuarkusTest
@SuppressWarnings("unchecked")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DummyResourceTest {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  DummyMockData dummyMockData;

  @Inject
  ElasticSearchTestHelper testHelper;

  @Test
  @Order(5)
  @Tag("create")
  @Tag("searchByCode")
  @Tag("searchByTitleTranslation")
  @Tag("dummyQueryTestAggregations")
  @Tag("dummyQueryTest")
  @Tag("deleteById")
  @Tag("deleteByQuery")
  public void create() throws IOException {
    List<Dummy> dummies = this.dummyMockData.createMockDummies();

    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(dummies)
      .when()
      .post("/api/internal/test/dummy/")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Dummy.class);

    assertEquals(8, dummies.size());
    List<String> eCodes = dummies.stream().map(DtoCode::getCode).toList();
    testHelper.waitForObjects("/dummy/_search", eCodes, 8);
  }

  @Test
  @Order(10)
  @Tag("searchByCode")
  public void searchByCode() {
    QueryParams params = QueryParams.builder().condition(
      or(
        field("code", "sad15s1a21sa21a51a"),
        field("code", "asdlasdadsa4dsds4d"),
        field("code", "4d5as45s1ds4d5ss8sd6s")
      )
    ).build();

    params.tryGetResultFilter().setSize(100);
    params.tryGetResultFilter().getContent().setTreeLevel(5);

    List<Dummy> dummies = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("editor1", "password")
      .and()
      .body(params)
      .when()
      .post("/api/public/test/dummy/query")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Dummy.class);

    assertEquals(3, dummies.size());
  }


  @Test
  @Order(20)
  @Tag("searchByTitleTranslation")
  public void searchByTitleTranslation() {
    QueryParams params = QueryParams.builder().condition(
      and(
        field("translations.lang", "de")
      )
    ).build();

    List<Dummy> dummies = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("editor1", "password")
      .and()
      .body(params)
      .when()
      .post("/api/public/test/dummy/query")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Dummy.class);

    assertEquals("4d5as45s1ds4d5ss8sd6s", dummies.getFirst().getCode());
    assertEquals("de", dummies.getFirst().getTranslations().iterator().next().getLang());
    assertEquals(1, dummies.size());
  }

  @Test
  @Order(30)
  @Tag("dummyQueryTestAggregations")
  public void dummyQueryTestAggregations() {
    QueryParams params = QueryParams.builder().aggregation(
      aggs(
        terms(
          "languages",
          "lang",
          count(
            "number",
            "lang"
          )
        )
      )
    ).build();

    Map<Object, Object> response = given()
      .contentType(ContentType.JSON)
      .accept(APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .body(params)
      .when()
      .post("/api/public/test/dummy/query")
      .then()
      .statusCode(200)
      .extract()
      .body()
      .jsonPath()
      .getMap("aggregations");

    assertEquals(8, ((List<?>) ((Map<Object, Object>)response.get("languages")).get("buckets")).size());
  }

  @Test
  @Order(50)
  @Tag("dummyQueryTest")
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
  @Tag("dummySearchNullTest")
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
        field("code", "JAVA101"),
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
  @Order(40)
  public void testWildcardSearch() {
    Dummy dummy1 = new Dummy();
    dummy1.setTitle("Introduction to Java");
    dummy1.setDescription("A comprehensive guide to Java programming.");
    dummy1.setLang("en");
    dummy1.setCreated(ZonedDateTime.now().minusDays(10));
    dummy1.setModified(ZonedDateTime.now());
    dummy1.setDeactivated(null);
    dummy1.setCode("dummy_code45");

    Dummy dummy2 = new Dummy();
    dummy2.setTitle("Advanced Python Techniques");
    dummy2.setDescription("Explore advanced concepts in Python programming.");
    dummy2.setLang("en");
    dummy2.setCreated(ZonedDateTime.now().minusDays(20));
    dummy2.setModified(ZonedDateTime.now().minusDays(5));
    dummy2.setDeactivated(null);
    dummy2.setCode("dummy_code46");

    Dummy dummy3 = new Dummy();
    dummy3.setTitle("Introduction to Machine Learning");
    dummy3.setDescription("An introductory course to machine learning and its applications.");
    dummy3.setLang("si");
    dummy3.setCreated(ZonedDateTime.now().minusMonths(2));
    dummy3.setModified(ZonedDateTime.now().minusDays(10));
    dummy3.setDeactivated(null);
    dummy3.setCode("dummy_code47");

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

    QueryParams s = QueryParams.builder().condition(
      or(
        wildcard("description", "comprehensive", true, 1.2f),
        wildcard("description", "concepts")
      )
    ).build();

    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(s)
      .when()
      .post("/api/public/test/dummy/query")
      .then()
      .statusCode(200)
      .log().all();

    log.info("Wildcard query: {}", s.getCondition());
  }

  @Test
  @Order(40)
  public void testMatchPhraseSearch() {

    QueryParams s = QueryParams.builder().condition(
      or(
        phrase("description", "comprehensive guide", 2),
        phrase("description", "concepts in ")
      )
    ).build();

    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(s)
      .when()
      .post("/api/public/test/dummy/query")
      .then()
      .statusCode(200)
      .log().all();

    log.info("Wildcard query: {}", s.getCondition());
  }

  @Test
  @Order(40)
  public void testAdvancedTextWithPhraseAndWildcard() {

    QueryParams s = QueryParams.builder().condition(
      or(
        phrase("description", "comprehensive guide", 2),
        wildcard("description", "con*epts"),
        advancedText("description", "\"conc*pts with phras*\"")
      )
    ).build();

    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(s)
      .when()
      .post("/api/public/test/dummy/query")
      .then()
      .statusCode(200)
      .log().all();

    log.info("Wildcard query: {}", s.getCondition());
  }

  @Test
  @Order(50)
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


  @Test
  @Order(60)
  @Tag("deleteById")
  public void deleteById() {
    CodeParams params1 = CodeParams.builder().code("dummy_code1").build();

    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("editor1", "password")
      .body(params1)
      .when()
      .delete("/api/internal/test/dummy/deleteById")
      .then()
      .statusCode(200)
      .log().all();
  }

  @Test
  @Order(70)
  @Tag("deleteByQuery")
  public void deleteByQuery() {
    QueryParams s = QueryParams.builder().condition(
      field("code", "dummy_code2")
    ).build();

    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("editor1", "password")
      .body(s)
      .when()
      .delete("/api/internal/test/dummy/deleteByQuery")
      .then()
      .statusCode(200)
      .log().all();
  }
}