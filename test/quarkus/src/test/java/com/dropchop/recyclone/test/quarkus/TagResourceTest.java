package com.dropchop.recyclone.test.quarkus;

import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 06. 22.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TagResourceTest {
  @Test
  @Order(10)
  public void createErrorMissingLang() {
    LanguageGroup languageGroupExYu = new LanguageGroup();
    languageGroupExYu.setName("ex_yu");

    LanguageGroup languageGroupSlavic = new LanguageGroup();
    languageGroupSlavic.setName("slavic");

    String resp = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(languageGroupExYu, languageGroupSlavic))
      .when()
      .post("/api/internal/tagging/tag")
      .then()
      .statusCode(500)
      .body("status.code", equalTo("error"))
      .body("status.message.code", equalTo("internal_error"))
      .body("status.message.text", equalTo("Missing language code in lang field for DTO!"))
      .body("data", empty())
      .extract().asPrettyString();
  }

  @Test
  @Order(20)
  public void create() {
    LanguageGroup languageGroupExYu = new LanguageGroup();
    languageGroupExYu.setName("ex_yu");
    languageGroupExYu.setLang("en");
    languageGroupExYu.setTitle("Ex Yugoslavic");

    LanguageGroup languageGroupSlavic = new LanguageGroup();
    languageGroupSlavic.setName("slavic");
    languageGroupSlavic.setLang("en");
    languageGroupSlavic.setTitle("Slavic");

    List<LanguageGroup> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(languageGroupExYu, languageGroupSlavic))
      .when()
      .post("/api/internal/tagging/tag")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", LanguageGroup.class);
    assertEquals(2, result.size());
    /*Role respRole = result.get(0);
    assertEquals(role, respRole);
    assertEquals(role.getTitle(), respRole.getTitle());
    assertEquals(role.getLang(), respRole.getLang());
    assertEquals(role.getTranslations(), respRole.getTranslations());*/

  }
}
