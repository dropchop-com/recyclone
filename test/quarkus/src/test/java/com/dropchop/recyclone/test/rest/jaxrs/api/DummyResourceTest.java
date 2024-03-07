package com.dropchop.recyclone.test.rest.jaxrs.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import jakarta.inject.Inject;
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
        .auth().preemptive().basic("user1", "password")
        .when()
        .get("/api/internal/test/dummy")
        .then()
        .statusCode(200)
        .log().all();
        //.body("[0].code", equalTo("sl")).extract().asPrettyString();
  }
}
