package com.dropchop.recyclone.quarkus.it.rest.dummy;

import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.rest.dummy.mock.DummyMockData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static com.dropchop.recyclone.base.api.model.invoke.Constants.ModifyPolicy.WAIT_FOR;
import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.count;
import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.terms;
import static com.dropchop.recyclone.base.api.model.query.Condition.*;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.*;
import static com.dropchop.recyclone.base.api.model.rest.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;
import static com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.ContentFilter.cf;
import static com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.rf;
import static com.dropchop.recyclone.quarkus.it.rest.Constants.*;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
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
  ObjectMapper mapper;

  private static final String Q_ENDPOINT = "/api/public/test/dummy/query";

  @BeforeEach
  public void setUp() {
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
      objectMapperConfig().jackson2ObjectMapperFactory((type, s) -> mapper)
    );
  }

  @Test
  @Order(5)
  @Tag("create")
  @Tag("searchByCode")
  @Tag("searchByTitleTranslation")
  @Tag("dummyQueryAggregations")
  @Tag("wildcardSearch")
  @Tag("matchPhraseSearch")
  @Tag("advancedTextSearch")
  @Tag("deleteById")
  @Tag("deleteByQuery")
  @Tag("aggregationsWithFilters")
  public void create() {
    List<Dummy> dummies = this.dummyMockData.createMockDummies();

    given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("admin1", TEST_PASSWORD)
      .and()
      .body(dummies)
      .when()
      .post("/api/internal/test/dummy?modify_policy=wait")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Dummy.class);

    assertEquals(8, dummies.size());
  }

  @Test
  @Order(10)
  @Tag("searchByCode")
  public void searchByCode() {
    QueryParams params = QueryParams.builder()
      .or(
        field("code", "sad15s1a21sa21a51a"),
        field("code", "asdlasdadsa4dsds4d"),
        field("code", "4d5as45s1ds4d5ss8sd6s")
      )
      .filter(rf().size(100).content(cf().treeLevel(5)))
      .build();

    List<Dummy> dummies = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic(EDITOR_USER, TEST_PASSWORD)
      .and()
      .body(params)
      .when()
      .post(Q_ENDPOINT)
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
    QueryParams params = QueryParams.builder()
      .and(
        field("translations.lang", "de"),
        field("created", lte(Iso8601.fromIso("2026-09-19T10:12:01.123")))
      )
      .build();

    List<Dummy> dummies = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic(EDITOR_USER, TEST_PASSWORD)
      .and()
      .body(params)
      .when()
      .post(Q_ENDPOINT)
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
  @Tag("dummyQueryAggregations")
  public void dummyQueryAggregations() {
    QueryParams params = QueryParams.builder().and(
     field("created", gte(Iso8601.fromIso("2020-09-19T10:12:01.123")))
    ).aggs(
      terms("languages", "lang",
        count("number", "lang")
      )
    ).build();

    Map<Object, Object> response = given()
      .contentType(ContentType.JSON)
      .accept(APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic(ADMIN_USER, TEST_PASSWORD)
      .body(params)
      .when()
      .post(Q_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body()
      .jsonPath()
      .getMap("aggregations");

    assertEquals(8, ((List<?>) ((Map<Object, Object>) response.get("languages")).get("buckets")).size());
  }

  @Test
  @Tag("aggregationsWithFilters")
  public void aggregationsWithFilters() {
    QueryParams params = QueryParams.builder().and(
      field("created", lte(Iso8601.fromIso("2026-09-19T10:12:01.123")))
    ).aggs(
      terms(
        "languages",
        "lang",
        filter(
          includes(List.of("zh"))
        )
      )
    ).build();

    Map<Object, Object> response = given()
      .contentType(ContentType.JSON)
      .accept(APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic(ADMIN_USER, TEST_PASSWORD)
      .body(params)
      .when()
      .post(Q_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body()
      .jsonPath()
      .getMap("aggregations");

    assertEquals(1, ((List<?>) ((Map<Object, Object>) response.get("languages")).get("buckets")).size());
  }

  @Test
  @Order(30)
  @Tag("dummySearchNull")
  public void dummySearchNull() {
    QueryParams params = QueryParams.builder()
      .and(
        field("languages.name", "ru"),
        field("created", lte(Iso8601.fromIso("2026-09-19T10:12:01.123")))
      )
      .build();

    List<Dummy> dummies = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", TEST_PASSWORD)
      .body(params)
      .when()
      .post(Q_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body()
      .jsonPath()
      .getList(".", Dummy.class);

    assertEquals(0, dummies.size());
  }

  @Test
  @Order(40)
  @Tag("wildcardSearch")
  public void wildcardSearch() {
    QueryParams s = QueryParams.builder()
      .or(
        wildcard("title", "Dum*", true, 1.2f),
        field("created", lte(Iso8601.fromIso("2026-09-19T10:12:01.123")))
      )
      .build();

    List<Dummy> dummies = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", TEST_PASSWORD)
      .body(s)
      .when()
      .post(Q_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body()
      .jsonPath()
      .getList(".", Dummy.class);

    assertEquals(8, dummies.size());
  }

  @Test
  @Order(40)
  @Tag("matchPhraseSearch")
  public void matchPhraseSearch() {
    QueryParams s = QueryParams.builder()
      .and(
        phrase("title", "Dummy 3", 2),
        field("created", lte(Iso8601.fromIso("2026-09-19T10:12:01.123")))
      )
      .build();

    List<Dummy> dummies = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic(USER_USER, TEST_PASSWORD)
      .body(s)
      .when()
      .post(Q_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body()
      .jsonPath()
      .getList(".", Dummy.class);

    assertEquals(1, dummies.size());
  }

  @Test
  @Order(40)
  @Tag("advancedTextSearch")
  public void advancedTextSearch() {
    QueryParams s = QueryParams.builder()
      .and(
        advancedText("title", "\"Dum* 5\""),
        field("created", lte(Iso8601.fromIso("2026-09-19T10:12:01.123")))
      )
      .build();

    List<Dummy> dummies = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic(USER_USER, TEST_PASSWORD)
      .body(s)
      .when()
      .post(Q_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body()
      .jsonPath()
      .getList(".", Dummy.class);

    assertEquals(1, dummies.size());
  }

  @Test
  @Order(60)
  @Tag("deleteById")
  public void deleteById() {
    CodeParams params1 = CodeParams.builder()
      .modifyPolicy(List.of(WAIT_FOR))
      .code("sad15s1a21sa21a51a")
      .build();

    Integer number_of_deleted = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic(EDITOR_USER, TEST_PASSWORD)
      .body(params1)
      .when()
      .delete("/api/internal/test/dummy/deleteById")
      .then()
      .statusCode(200)
      .extract()
      .body()
      .jsonPath()
      .getInt(".");

    assertEquals(1, number_of_deleted);
  }

  @Test
  @Order(70)
  @Tag("deleteByQuery")
  public void deleteByQuery() {
    QueryParams s = QueryParams.builder()
      .modifyPolicy(List.of(WAIT_FOR))
      .and(
        field("code", "4d5as45s1ds4d5ss8sd6s")
      )
      .build();

    Integer number_of_deleted = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic(EDITOR_USER, TEST_PASSWORD)
      .body(s)
      .when()
      .delete("/api/internal/test/dummy/deleteByQuery")
      .then()
      .statusCode(200)
      .extract()
      .body()
      .jsonPath()
      .getInt(".");

    assertEquals(1, number_of_deleted);
  }
}