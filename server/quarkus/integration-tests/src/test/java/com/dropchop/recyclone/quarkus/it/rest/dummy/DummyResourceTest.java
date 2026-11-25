package com.dropchop.recyclone.quarkus.it.rest.dummy;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.rest.dummy.mock.DummyMockData;
import com.dropchop.recyclone.quarkus.runtime.rest.jaxrs.ServiceErrorExceptionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.security.ForbiddenException;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static com.dropchop.recyclone.base.api.model.invoke.Constants.ModifyPolicy.WAIT_FOR;
import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.count;
import static com.dropchop.recyclone.base.api.model.query.Aggregation.Wrapper.terms;
import static com.dropchop.recyclone.base.api.model.query.Condition.*;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.*;
import static com.dropchop.recyclone.base.api.model.rest.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;
import static com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.ContentFilter.content;
import static com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.result;
import static com.dropchop.recyclone.quarkus.it.rest.Constants.*;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.HttpHeaders.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 03. 24.
 */
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
  private static final String CONDITIONAL_ENDPOINT = "/api/public/test/dummy/conditional/updated";

  @BeforeEach
  public void setUp() {
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
      objectMapperConfig().jackson2ObjectMapperFactory((type, s) -> mapper)
    );
  }

  private ValidatableResponse conditionalWrite(String ifMatch) {
    RequestSpecification request = given()
        .contentType(ContentType.JSON)
        .accept(APPLICATION_JSON_DROPCHOP_RESULT)
        .auth().preemptive().basic("admin1", TEST_PASSWORD)
        .body("{\"expectedRevision\":\"forged\"}");
    if (ifMatch != null) {
      request.header(IF_MATCH, ifMatch);
    }
    return request.put(CONDITIONAL_ENDPOINT).then();
  }

  @Test
  void handlesOptimisticConcurrencyPreconditions() {
    conditionalWrite(null)
        .statusCode(428)
        .body("status.message.code", equalTo(ErrorCode.precondition_required_error.name()))
        .header(ETAG, nullValue());
    conditionalWrite("W/\"current\"")
        .statusCode(400)
        .body("status.message.code", equalTo(ErrorCode.precondition_validation_error.name()))
        .header(ETAG, nullValue());
    conditionalWrite("\"previous\"")
        .statusCode(412)
        .body("status.message.code", equalTo(ErrorCode.precondition_failed_error.name()))
        .header(ETAG, nullValue());
    String current = conditionalWrite("\"current\"")
        .statusCode(200)
        .header(ETAG, equalTo("\"updated\""))
        .extract().body().asString();
    assertEquals(current, conditionalWrite("\"current\"").statusCode(200).extract().body().asString());
  }

  @Test
  void reportsRetryAfterWhenSpecified() {
    ServiceErrorExceptionMapper exceptionMapper = new ServiceErrorExceptionMapper();
    ServiceException failure = new ServiceException(ErrorCode.service_unavailable_error, "Temporarily unavailable");
    try (Response response = exceptionMapper.toResponse(failure.retryAfter(0))) {
      assertEquals(503, response.getStatus());
      assertEquals("0", response.getHeaderString(RETRY_AFTER));
    }

    failure.setRetryAfter(null);
    try (Response response = exceptionMapper.toResponse(failure)) {
      assertEquals(503, response.getStatus());
      assertNull(response.getHeaderString(RETRY_AFTER));
    }
  }

  @Test
  void mapsInsufficientPermissionsToForbidden() {
    ServiceErrorExceptionMapper exceptionMapper = new ServiceErrorExceptionMapper();
    ServiceException failure = new ServiceException(ErrorCode.insufficient_permissions_error, "Role required");
    try (Response response = exceptionMapper.toResponse(failure)) {
      assertEquals(403, response.getStatus());
    }
    try (Response response = exceptionMapper.toResponse(new ForbiddenException())) {
      assertEquals(403, response.getStatus());
      assertTrue(response.getEntity().toString().contains(ErrorCode.insufficient_permissions_error.name()));
    }
    try (Response response = exceptionMapper.toResponse(new ServiceException(ErrorCode.authentication_error, "Login required"))) {
      assertEquals(401, response.getStatus());
    }
  }

  @Test
  @Order(5)
  @Tag("DummyResourceTest.create")
  @Tag("DummyResourceTest.searchByCode")
  @Tag("DummyResourceTest.searchByTitleTranslation")
  @Tag("DummyResourceTest.dummyQueryAggregations")
  @Tag("DummyResourceTest.wildcardSearch")
  @Tag("DummyResourceTest.matchPhraseSearch")
  @Tag("DummyResourceTest.advancedTextSearch")
  @Tag("DummyResourceTest.deleteById")
  @Tag("DummyResourceTest.deleteByQuery")
  @Tag("DummyResourceTest.aggregationsWithFilters")
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
  @Tag("DummyResourceTest.searchByCode")
  public void searchByCode() {
    QueryParams params = QueryParams.builder()
      .or(
        field("code", "sad15s1a21sa21a51a"),
        field("code", "asdlasdadsa4dsds4d"),
        field("code", "4d5as45s1ds4d5ss8sd6s")
      )
      .filter(result(content(5)).size(100))
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
  @Tag("DummyResourceTest.searchByTitleTranslation")
  public void searchByTitleTranslation() {
    QueryParams params = QueryParams.builder()
      .and(
        field("translations.lang", "de"),
        field("created", gte(Iso8601.fromIso("2025-09-19T10:12:01.123")))
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
  @Tag("DummyResourceTest.dummyQueryAggregations")
  public void dummyQueryAggregations() {
    QueryParams params = QueryParams.builder().and(
     field("created", gte(Iso8601.fromIso("2025-09-19T10:12:01.123")))
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
  @Tag("DummyResourceTest.aggregationsWithFilters")
  public void aggregationsWithFilters() {
    QueryParams params = QueryParams.builder().and(
      field("created", gte(Iso8601.fromIso("2025-09-19T10:12:01.123")))
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
  @Tag("DummyResourceTest.dummySearchNull")
  public void dummySearchNull() {
    QueryParams params = QueryParams.builder()
      .and(
        field("languages.name", "ru"),
        field("created", gte(Iso8601.fromIso("2025-09-19T10:12:01.123")))
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
  @Tag("DummyResourceTest.wildcardSearch")
  public void wildcardSearch() {
    QueryParams s = QueryParams.builder()
      .or(
        wildcard("title", "Dum*", true, 1.2f),
        field("created", gte(Iso8601.fromIso("2025-09-19T10:12:01.123")))
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
  @Tag("DummyResourceTest.matchPhraseSearch")
  public void matchPhraseSearch() {
    QueryParams s = QueryParams.builder()
      .and(
        phrase("title", "Dummy 3", 2),
        field("created", gte(Iso8601.fromIso("2025-01-19T10:12:01.123")))
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
  @Tag("DummyResourceTest.advancedTextSearch")
  @Disabled
  public void advancedTextSearch() {
    QueryParams s = QueryParams.builder()
      .and(
        advancedText("title", "\"Dum* 5\""),
        field("created", gte(Iso8601.fromIso("2025-09-19T10:12:01.123")))
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
  @Tag("DummyResourceTest.deleteById")
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
  @Tag("DummyResourceTest.deleteByQuery")
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