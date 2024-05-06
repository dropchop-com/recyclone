package com.dropchop.recyclone.quarkus.it.rest;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 03. 24.
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
}
