package com.dropchop.recyclone.quarkus.it.rest.dummy;

import com.dropchop.recyclone.base.api.model.invoke.Constants;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.rest.dummy.mock.DummyMockData;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.count;
import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.terms;
import static com.dropchop.recyclone.base.api.model.query.Condition.*;
import static com.dropchop.recyclone.base.api.model.rest.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;
import static com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.ContentFilter.cf;
import static com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.rf;
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
    QueryParams params = QueryParams.builder()
        .and(
          field("translations.lang", "de")
        )
        .build();

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
  @Tag("dummyQueryAggregations")
  public void dummyQueryAggregations() {
    QueryParams params = QueryParams.builder()
        .aggs(
          terms("languages", "lang",
            count("number", "lang")
          )
        )
        .build();

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
  @Tag("dummySearchNull")
  public void dummySearchNull() {
    QueryParams params = QueryParams.builder()
        .condition(field("languages.name", "ru"))
        .build();

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
  @Tag("wildcardSearch")
  public void wildcardSearch() {
    QueryParams s = QueryParams.builder()
        .or(
            wildcard("title", "Dum*", true, 1.2f)
        )
        .build();

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

    assertEquals(8, dummies.size());
  }

  @Test
  @Order(40)
  @Tag("matchPhraseSearch")
  public void matchPhraseSearch() {

    QueryParams s = QueryParams.builder()
        .or(
            phrase("title", "Dummy 3", 2)
        )
        .build();

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
  @Tag("advancedTextSearch")
  public void advancedTextSearch() {

    QueryParams s = QueryParams.builder()
        .or(
            advancedText("title", "\"Dum* 5\"")
        )
        .build();

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
  @Order(60)
  @Tag("deleteById")
  public void deleteById() {
    CodeParams params1 = CodeParams.builder()
        .modifyPolicy(List.of(Constants.ModifyPolicy.WAIT_FOR))
        .code("sad15s1a21sa21a51a")
        .build();

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
    //testHelper.waitForObjects("/dummy/_search", List.of("sad15s1a21sa21a51a"), 0);
  }

  @Test
  @Order(70)
  @Tag("deleteByQuery")
  public void deleteByQuery() {
    QueryParams s = QueryParams.builder()
        .modifyPolicy(List.of(Constants.ModifyPolicy.WAIT_FOR))
        .condition(
          field("code", "4d5as45s1ds4d5ss8sd6s")
        )
        .build();

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
  }
}