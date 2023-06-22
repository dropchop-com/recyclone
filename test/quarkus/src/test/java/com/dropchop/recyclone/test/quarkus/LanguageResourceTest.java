package com.dropchop.recyclone.test.quarkus;

import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 12. 21.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LanguageResourceTest {

  @Test
  @Order(10)
  public void languageRestByCodeEndpoint() {
    given()
      //.log().all()
      .auth().preemptive().basic("user1", "password")
      .when()
      .get("/api/public/localization/language/sl")
      .then()
      .statusCode(200)
      .log().all()
      .body("[0].code", equalTo("sl")).extract().asPrettyString();
  }

  @Test
  @Order(20)
  public void languageRestByCodeEndpointApiKey() {
    given()
      //.log().all()
      .header("X-API-Key", "admintoken1")
      .when()
      .get("/api/public/localization/language/sl")
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
      .get("/api/public/localization/language/sl?api_key=admintoken1")
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
      .get("/api/public/localization/language/sl")
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
      .auth().preemptive().basic("user1", "password")
      .when()
      .get("/api/public/localization/language/sl")
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
      .auth().preemptive().basic("user1", "password")
      .accept(MediaType.APPLICATION_JSON)
      .when()
      .get("/api/public/localization/language")
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
  @Order(70)
  public void languagesRestUnauthorized() {
    given()
      .when()
      .get("/api/public/localization/language")
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
      .auth().preemptive().basic("user1", "password")
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .when()
      .get("/api/public/localization/language")
      .then()
      .statusCode(200)
      .log().all()
      .body("status.code", equalTo("success"))
      .extract()
      .jsonPath().getList("data", Language.class);
    assertEquals(7, languages.size());
    assertEquals(new Language("en"), languages.get(0));
    assertNotNull(languages.get(0).getLang());
    assertNotNull(languages.get(0).getTitle());
    assertNotNull(languages.get(0).getCreated());
    assertNotNull(languages.get(0).getModified());
    assertNull(languages.get(0).getDeactivated());
    assertNotNull(languages.get(0).getTranslations());
    assertEquals(new Language("sl"), languages.get(1));
    assertEquals(new Language("sl-nonstandard"), languages.get(2));
    assertEquals(new Language("hr"), languages.get(3));
  }

  @Test
  @Order(80)
  public void languagesEndpointWithSortPageFieldFilter() {
    List<Language> languages = given()
      //.log().all()
      .auth().preemptive().basic("user1", "password")
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .when()
      .get("/api/public/localization/language?from=4&size=2&sort=-code&c_fields=code&c_fields=title")
      .then()
      .statusCode(200)
      .body("status.code", equalTo("success"))
      .extract()
      .jsonPath().getList("data", Language.class);
    assertEquals(2, languages.size());
    assertEquals(new Language("sl"), languages.get(0));
    assertNull(languages.get(0).getLang());
    assertNotNull(languages.get(0).getTitle());
    assertNull(languages.get(0).getCreated());
    assertNull(languages.get(0).getModified());
    assertNull(languages.get(0).getDeactivated());
    assertNull(languages.get(0).getTranslations());
    assertEquals(new Language("hr"), languages.get(1));
  }

  /*
  @Test
  @Order(7)
  public void languageByUuidEndpoint() {
    List<Language> languages = given()
      .auth().preemptive().basic("user1", "password")
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .when()
      .get("/api/language/4c4de73e-fbdc-321a-a15b-3c084204a915")
      .then()
      .statusCode(200)
      .body("status.code", equalTo("success"))
      .extract()
      .jsonPath().getList("data", Language.class);
    assertEquals(1, languages.size());
    assertEquals(new Language("sl"), languages.get(0));
  }

  @Test
  @Order(8)
  public void languageByMissingUuidEndpoint() {
    given()
      .auth().preemptive().basic("user1", "password")
      .when()
      .get("/api/language/5c6de73e-fbdc-321a-a15b-3c084204a915")
      .then()
      .statusCode(404)
      .body("status.code", equalTo("error"))
      .body("status.message.code", equalTo("not_found_error"));
  }
  */

  @Test
  @Order(90)
  public void languagesPostEndpoint() {
    Language language = new Language("bs");
    language.setTitle("en", "Bosnian");
    language.addTranslation(new TitleTranslation("sl", "Bosanski"));
    List<Language> languages = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("editor1", "password")
      .and()
      .body(List.of(language))
      .when()
      .post("/api/internal/localization/language")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Language.class);
    assertEquals(1, languages.size());
    assertEquals(language, languages.get(0));
  }

  @Test
  @Order(100)
  public void languagesDeleteEndpoint() {
    List<Language> languages = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .header("Authorization", "Bearer editortoken1")
      //.auth().preemptive().basic("editor1", "password")
      .and()
      .body(List.of(new Language("bs")))
      .when()
      .delete("/api/internal/localization/language")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Language.class);
    assertEquals(1, languages.size());
    assertEquals(new Language("bs"), languages.get(0));
  }

  @Test
  @Order(110)
  public void languagesEndpointWithLevelFilter() {
    List<Language> languages = given()
      //.log().all()
      .auth().preemptive().basic("user1", "password")
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .when()
      .get("/api/public/localization/language?from=4&size=2&sort=-code&c_level=3.nest_id")
      .then()
      .statusCode(200)
      .body("status.code", equalTo("success"))
      .log().all()
      .extract()
      .jsonPath().getList("data", Language.class);
    assertEquals(2, languages.size());
    assertEquals(new Language("sl"), languages.get(0));
    /*assertNull(languages.get(0).getLang());
    assertNotNull(languages.get(0).getTitle());
    assertNull(languages.get(0).getCreated());
    assertNull(languages.get(0).getModified());
    assertNull(languages.get(0).getDeactivated());
    assertNull(languages.get(0).getTranslations());
    assertEquals(new Language("hr"), languages.get(1));*/
  }
}
