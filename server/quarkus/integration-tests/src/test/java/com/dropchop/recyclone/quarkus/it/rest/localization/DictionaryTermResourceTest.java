package com.dropchop.recyclone.quarkus.it.rest.localization;

import com.dropchop.recyclone.base.api.model.rest.MediaType;
import com.dropchop.recyclone.base.dto.model.invoke.CodeTitleParams;
import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.base.dto.model.localization.DictionaryTerm;
import com.dropchop.recyclone.quarkus.it.rest.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
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
public class DictionaryTermResourceTest {


  private static final String DT_CODE = "test_dictionary_term";
  private static final String DT_TITLE = "test dictionary term";
  private static final String DT_TITLE_UPDATED = "test dictionary term test dictionary term";


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
  public void createDictionaryTerm() {
    DictionaryTerm dt = new  DictionaryTerm();
    dt.setCode(DT_CODE);
    dt.setLang("en");
    dt.setTitle(DT_TITLE);
    dt.setTitle("en", DT_TITLE);
    dt.setCreated(ZonedDateTime.now());

    given()
        .header("X-Recyclone-API-Key", "admintoken1")
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .body(List.of(dt))
        .post(Constants.DICTIONARY_TERM_INTERNAL_ENDPOINT)
        .then()
        .statusCode(200)
        //.log().all()
        .extract()
        .body().jsonPath().getList(".", DictionaryTerm.class);


    CodeTitleParams parameters = new CodeTitleParams();

    parameters.setCodes(List.of(DT_CODE));

    List<DictionaryTerm> terms = given()
        .header("X-Recyclone-API-Key", "admintoken1")
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .body(parameters)
        .post(Constants.DICTIONARY_TERM_PUBLIC_ENDPOINT + "/search")
        .then()
        .statusCode(200)
        //.log().all()
        .extract()
        .body().jsonPath().getList(".", DictionaryTerm.class);

    assertNotNull(terms);
    assertEquals(1, terms.size());
    assertEquals(DT_CODE, terms.get(0).getCode());
    assertEquals(DT_TITLE, terms.get(0).getTranslationOrTitle("en"));

  }


  @Test
  @Order(20)
  public void updateDictionaryTerm() {
    DictionaryTerm dt = new  DictionaryTerm();
    dt.setCode(DT_CODE);
    dt.setLang("en");
    dt.setTitle(DT_TITLE_UPDATED);
    dt.setTitle("en", DT_TITLE_UPDATED);
    dt.setCreated(ZonedDateTime.now());

    given()
        .header("X-Recyclone-API-Key", "admintoken1")
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .body(List.of(dt))
        .put(Constants.DICTIONARY_TERM_INTERNAL_ENDPOINT)
        .then()
        .statusCode(200)
        //.log().all()
        .extract()
        .body().jsonPath().getList(".", DictionaryTerm.class);

    CodeTitleParams parameters = new CodeTitleParams();

    parameters.setCodes(List.of(DT_CODE));


    List<DictionaryTerm> terms = given()
        .header("X-Recyclone-API-Key", "admintoken1")
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .body(parameters)
        .post(Constants.DICTIONARY_TERM_PUBLIC_ENDPOINT + "/search")
        .then()
        .statusCode(200)
        //.log().all()
        .extract()
        .body().jsonPath().getList(".", DictionaryTerm.class);

    assertNotNull(terms);
    assertEquals(1, terms.size());
    assertEquals(DT_CODE, terms.get(0).getCode());
    assertEquals(DT_TITLE_UPDATED, terms.get(0).getTranslationOrTitle("en"));
  }

  @Test
  @Order(30)
  public void deleteDictionaryTerm() {

    CodeTitleParams parameters = new CodeTitleParams();

    parameters.setCodes(List.of(DT_CODE));

    List<DictionaryTerm> terms = given()
        .header("X-Recyclone-API-Key", "admintoken1")
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .body(parameters)
        .post(Constants.DICTIONARY_TERM_PUBLIC_ENDPOINT + "/search")
        .then()
        .statusCode(200)
        //.log().all()
        .extract()
        .body().jsonPath().getList(".", DictionaryTerm.class);

    assertNotNull(terms);
    assertEquals(1, terms.size());
    assertEquals(DT_CODE, terms.get(0).getCode());
    assertEquals(DT_TITLE_UPDATED, terms.get(0).getTranslationOrTitle("en"));

    given()
        .header("X-Recyclone-API-Key", "admintoken1")
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .body(terms)
        .delete(Constants.DICTIONARY_TERM_INTERNAL_ENDPOINT)
        .then()
        .statusCode(200)
        //.log().all()
        .extract()
        .body().jsonPath().getList(".", DictionaryTerm.class);

  }

}
