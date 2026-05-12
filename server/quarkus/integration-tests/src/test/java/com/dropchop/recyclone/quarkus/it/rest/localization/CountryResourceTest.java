package com.dropchop.recyclone.quarkus.it.rest.localization;

import com.dropchop.recyclone.base.api.model.rest.MediaType;
import com.dropchop.recyclone.base.dto.model.invoke.CodeTitleParams;
import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.quarkus.it.rest.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 12. 5. 26.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CountryResourceTest {

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
  public void countrySearchRestByCodeEndpoint() {

    CodeTitleParams parameters = new CodeTitleParams();

    parameters.setCodes(List.of("SI", "HR"));

    List<Country> countries = given()
        //.log().all()
        .header("X-Recyclone-API-Key", "admintoken1")
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .body(parameters)
        .post(Constants.COUNTRY_ENDPOINT + "/search")
        .then()
        .statusCode(200)
        //.log().all()
        .extract()
        .body().jsonPath().getList(".", Country.class);

    assertNotNull(countries);
    assertEquals(2, countries.size());
  }

}
