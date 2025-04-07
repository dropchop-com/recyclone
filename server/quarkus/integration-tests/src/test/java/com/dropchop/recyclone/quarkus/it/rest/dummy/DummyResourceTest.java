package com.dropchop.recyclone.quarkus.it.rest.dummy;

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
import java.util.List;
import java.util.Map;

import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.*;
import static com.dropchop.recyclone.base.api.model.query.Condition.*;
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
  @Tag("testWildcardSearch")
  @Tag("testMatchPhraseSearch")
  @Tag("testAdvancedTextSearch")
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

    assertEquals(8, ((List<?>) ((Map<Object, Object>) response.get("languages")).get("buckets")).size());
  }

  @Test
  @Order(30)
  @Tag("dummySearchNullTest")
  public void dummySearchNullTest() {
    QueryParams params = QueryParams.builder().condition(
      field("languages.name", "ru")
    ).build();

    List<Dummy> dummies = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(params)
      .when()
      .post("/api/public/test/dummy/query")
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
  @Tag("testWildcardSearch")
  public void testWildcardSearch() {
    QueryParams s = QueryParams.builder().condition(
      or(
        wildcard("title", "Title", true, 1.2f)
      )
    ).build();

    List<Dummy> dummies = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(s)
      .when()
      .post("/api/public/test/dummy/query")
      .then()
      .statusCode(200)
      .extract()
      .body()
      .jsonPath()
      .getList(".", Dummy.class);

    assertEquals(6, dummies.size());
  }

  @Test
  @Order(40)
  @Tag("testMatchPhraseSearch")
  public void testMatchPhraseSearch() {

    QueryParams s = QueryParams.builder().condition(
      or(
        phrase("title", "Title 1", 2)
      )
    ).build();

    List<Dummy> dummies = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(s)
      .when()
      .post("/api/public/test/dummy/query")
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
  @Tag("testAdvancedTextSearch")
  public void testAdvancedTextSearch() {

    QueryParams s = QueryParams.builder().condition(
      or(
        advancedText("title", "\"Tit*l*\"")
      )
    ).build();

    List<Dummy> dummies = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("user1", "password")
      .body(s)
      .when()
      .post("/api/public/test/dummy/query")
      .then()
      .statusCode(200)
      .extract()
      .body()
      .jsonPath()
      .getList(".", Dummy.class);

    assertEquals(3, dummies.size());
  }

  @Test
  @Order(60)
  @Tag("deleteById")
  public void deleteById() throws IOException {
    CodeParams params1 = CodeParams.builder().code("sad15s1a21sa21a51a").build();

    Integer number_of_deleted = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("editor1", "password")
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
    testHelper.waitForObjects("/dummy/_search", List.of("sad15s1a21sa21a51a"), 0);
  }

  @Test
  @Order(70)
  @Tag("deleteByQuery")
  public void deleteByQuery() throws IOException {
    QueryParams s = QueryParams.builder().condition(
      field("code", "4d5as45s1ds4d5ss8sd6s")
    ).build();

    Integer number_of_deleted = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("editor1", "password")
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
    testHelper.waitForObjects("/dummy/_search", List.of("4d5as45s1ds4d5ss8sd6s"), 0);
  }
}