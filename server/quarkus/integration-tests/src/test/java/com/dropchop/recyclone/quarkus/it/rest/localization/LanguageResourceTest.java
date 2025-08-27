package com.dropchop.recyclone.quarkus.it.rest.localization;

import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.base.dto.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.dto.model.tagging.LanguageGroup;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import com.dropchop.recyclone.quarkus.it.rest.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 12. 21.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LanguageResourceTest {

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
  public void languageRestByCodeEndpoint() {
    given()
      //.log().all()
      .auth().preemptive().basic(Constants.USER_USER, Constants.TEST_PASSWORD)
      .when()
      .get(Constants.LANG_ENDPOINT + "/sl")
      .then()
      .statusCode(200)
      .log().all()
      .body("[0].code", equalTo("sl")).extract().asPrettyString();
  }

  @Test
  @Order(20)
  @SuppressWarnings("UastIncorrectHttpHeaderInspection")
  public void languageRestByCodeEndpointApiKey() {
    given()
      //.log().all()
      .header("X-Recyclone-API-Key", "admintoken1")
      .when()
      .get(Constants.LANG_ENDPOINT + "/sl")
      .then()
      .statusCode(200)
      .body("[0].code", equalTo("sl"));
  }

  @Test
  @Order(30)
  public void languageRestByCodeEndpointApiKey2() {
    given()
      //.log().all()
      .when()
      .get(Constants.LANG_ENDPOINT + "/sl?x-recyclone-api-key=admintoken1")
      .then()
      .statusCode(200)
      .body("[0].code", equalTo("sl"));
  }

  @Test
  @Order(40)
  public void languageRestByCodeEndpointBearer() {
    given()
      //.log().all()
      .header("Authorization", "Bearer admintoken1")
      .when()
      .get(Constants.LANG_ENDPOINT + "/sl")
      .then()
      .statusCode(200)
      .body("[0].code", equalTo("sl"));
  }

  @Test
  @Order(50)
  public void languageWrappedByCodeEndpoint() {
    given()
      //.log().all()
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic(Constants.USER_USER, Constants.TEST_PASSWORD)
      .when()
      .get(Constants.LANG_ENDPOINT + "/sl")
      .then()
      .statusCode(200)
      .body("status.code", equalTo("success"))
      .body("data[0].code", equalTo("sl"));
  }

  @Test
  @Order(60)
  public void languagesRestEndpoint() {
    List<Language> languages = given()
      //.log().all()
      .auth().preemptive().basic(Constants.USER_USER, Constants.TEST_PASSWORD)
      .accept(MediaType.APPLICATION_JSON)
      .when()
      .get(Constants.LANG_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Language.class);
    assertEquals(7, languages.size());
    assertEquals(new Language("en"), languages.get(0));
    assertEquals(new Language("sl"), languages.get(1));
    assertEquals(new Language("sl-nonstandard"), languages.get(2));
    assertEquals(new Language("hr"), languages.get(3));
  }

  @Test
  @Order(65)
  public void languagesRestEndpointWithTags() {
    List<Language> languages = given()
        //.log().all()
        .auth().preemptive().basic(Constants.USER_USER, Constants.TEST_PASSWORD)
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .get(Constants.LANG_ENDPOINT + "?c_level=10")
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList(".", Language.class);
    assertEquals(new Language("sl"), languages.get(1));
    Language language = languages.get(1);
    List<Tag> tags = language.getTags();
    assertEquals(1, tags.size());
    LanguageGroup group = (LanguageGroup)tags.getFirst();
    assertEquals("slavic", group.getName());
    assertEquals(UUID.fromString("c73847a8-836a-3ad3-b4f8-4a331248088d"), group.getUuid());
    assertEquals("c73847a8-836a-3ad3-b4f8-4a331248088d", group.getId());
  }

  @Test
  @Order(70)
  public void languagesRestUnauthorized() {
    given()
      .when()
      .get(Constants.LANG_ENDPOINT)
      .then()
      .statusCode(401)
      .body("status.code", equalTo("error"))
      .body("status.message.code", equalTo("authentication_error"));
  }

  @Test
  @Order(80)
  public void languagesEndpoint() {
    List<Language> languages = given()
      //.log().all()
      .auth().preemptive().basic(Constants.USER_USER, Constants.TEST_PASSWORD)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .when()
      .get(Constants.LANG_ENDPOINT)
      .then()
      .statusCode(200)
      .log().all()
      .body("status.code", equalTo("success"))
      .extract()
      .jsonPath().getList("data", Language.class);
    assertEquals(7, languages.size());
    Language language = languages.getFirst();
    assertEquals(new Language("en"), language);
    assertNotNull(language.getLang());
    assertNotNull(language.getTitle());
    assertNotNull(language.getCreated());
    assertNotNull(language.getModified());
    assertNull(language.getDeactivated());
    assertNotNull(language.getTranslations());
    assertEquals(new Language("sl"), languages.get(1));
    assertEquals(new Language("sl-nonstandard"), languages.get(2));
    assertEquals(new Language("hr"), languages.get(3));
  }

  @Test
  @Order(80)
  public void languagesEndpointWithSortPageFieldFilter() {
    List<Language> languages = given()
      //.log().all()
      .auth().preemptive().basic(Constants.USER_USER, Constants.TEST_PASSWORD)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .when()
      .get(Constants.LANG_ENDPOINT + "?from=4&size=2&sort=-code&c_fields=code&c_fields=title")
      .then()
      .statusCode(200)
      .body("status.code", equalTo("success"))
      .extract()
      .jsonPath().getList("data", Language.class);
    assertEquals(2, languages.size());
    Language language = languages.getFirst();
    assertEquals(new Language("sl"), language);
    assertNull(language.getLang());
    assertNotNull(language.getTitle());
    assertNull(language.getCreated());
    assertNull(language.getModified());
    assertNull(language.getDeactivated());
    assertNull(language.getTranslations());
    assertEquals(new Language("hr"), languages.get(1));
  }

  @Test
  @Order(90)
  @org.junit.jupiter.api.Tag("languagesPostEndpoint")
  @org.junit.jupiter.api.Tag("languagesDeleteEndpoint")
  public void languagesPostEndpoint() {
    Language language = new Language("bs");
    language.setTitle("en", "Bosnian");
    language.addTranslation(new TitleTranslation("sl", "Bosanski"));

    List<Language> languages = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("editor1", Constants.TEST_PASSWORD)
      .and()
      .body(List.of(language))
      .when()
      .post("/api/internal/localization/language")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Language.class);
    assertEquals(1, languages.size());
    assertEquals(language, languages.getFirst());
  }

  @Test
  @Order(100)
  @org.junit.jupiter.api.Tag("languagesDeleteEndpoint")
  public void languagesDeleteEndpoint() {
    List<Language> languages = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .header("Authorization", "Bearer editortoken1")
      //.auth().preemptive().basic("editor1", ANY_PASSWORD)
      .and()
      .body(List.of(new Language("bs")))
      .when()
      .delete("/api/internal/localization/language")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Language.class);
    assertEquals(1, languages.size());
    assertEquals(new Language("bs"), languages.getFirst());
  }

  @Test
  @Order(110)
  public void languagesEndpointWithLevelFilter() {
    List<Language> languages = given()
      //.log().all()
      .auth().preemptive().basic(Constants.USER_USER, Constants.TEST_PASSWORD)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .when()
      .get(Constants.LANG_ENDPOINT + "?from=4&size=2&sort=-code&c_level=3.nest_id")
      .then()
      .statusCode(200)
      .body("status.code", equalTo("success"))
      .log().all()
      .extract()
      .jsonPath().getList("data", Language.class);
    assertEquals(2, languages.size());
    assertEquals(new Language("sl"), languages.getFirst());
    /*assertNull(languages.get(0).getLang());
    assertNotNull(languages.get(0).getTitle());
    assertNull(languages.get(0).getCreated());
    assertNull(languages.get(0).getModified());
    assertNull(languages.get(0).getDeactivated());
    assertNull(languages.get(0).getTranslations());
    assertEquals(new Language("hr"), languages.get(1));*/
  }
}
