package com.dropchop.recyclone.quarkus.it.rest.tagging;

import com.dropchop.recyclone.base.api.model.attr.*;
import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.dropchop.recyclone.base.api.model.utils.Uuid;
import com.dropchop.recyclone.base.dto.model.tagging.LanguageGroup;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import com.dropchop.recyclone.quarkus.it.model.dto.DummyTag;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import jakarta.inject.Inject;
import java.util.List;

import static com.dropchop.recyclone.quarkus.it.rest.Constants.*;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 06. 22.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TagResourceTest {

  @Inject
  ObjectMapper mapper;

  @BeforeEach
  public void setUp() {
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
        objectMapperConfig().jackson2ObjectMapperFactory((type, s) -> mapper
    ));
  }

  @Test
  @Order(10)
  public void createErrorMissingLang() {
    LanguageGroup languageGroupExYu = new LanguageGroup();
    languageGroupExYu.setName("ex_yu");
    assertEquals("b6a5a51d-987f-3a84-b3da-6314e9dc97e4", languageGroupExYu.getUuid().toString());

    LanguageGroup languageGroupSlavic = new LanguageGroup();
    languageGroupSlavic.setName("slavic");
    assertEquals("c73847a8-836a-3ad3-b4f8-4a331248088d", languageGroupSlavic.getUuid().toString());

    given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(languageGroupExYu, languageGroupSlavic))
      .when()
      .post(TAG_ENDPOINT)
      .then()
      .statusCode(500)
      .body("status.code", equalTo("error"))
      .body("status.message.code", equalTo("internal_error"))
      .body("status.message.text", equalTo("Missing language code in lang field for DTO!"))
      .body("data", empty())
      .extract().asPrettyString();
  }

  @Test
  @Order(15)
  public void createNoErrorMissingLang() {
    DummyTag dummyTag1 = new DummyTag();
    dummyTag1.setName("dumdum");
    assertEquals(
        Uuid.getNameBasedV3(DummyTag.class.getSimpleName() + '.' + dummyTag1.getName()).toString(),
        dummyTag1.getUuid().toString()
    );

    DummyTag dummyTag2 = new DummyTag();
    dummyTag2.setName("limo_driver");
    assertEquals(
        Uuid.getNameBasedV3(DummyTag.class.getSimpleName() + '.' + dummyTag2.getName()).toString(),
        dummyTag2.getUuid().toString()
    );

    List<DummyTag> result = given()
        //.log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
        //.header("Authorization", "Bearer editortoken1")
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(List.of(dummyTag1, dummyTag2))
        .when()
        .post(TAG_ENDPOINT)
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList("data", DummyTag.class);
    assertEquals(2, result.size());
  }

  @Test
  @Order(20)
  @Tag("TagResourceTest.create")
  @Tag("TagResourceTest.removeAttribute")
  public void create() {
    //LanguageGroup languageGroupSlavic = new LanguageGroup();
    //languageGroupSlavic.setName("slavic");
    //--languageGroupSlavic.setLang("en");
    //--languageGroupSlavic.setTitle("Slavic");

    LanguageGroup languageGroupSSlavic = new LanguageGroup();
    languageGroupSSlavic.setName("southern_slavic");
    languageGroupSSlavic.setLang("en");
    languageGroupSSlavic.setTitle("Southern Slavic");
    assertEquals("0fe038f8-6f6f-3b71-97db-4d00b7a0ca9f", languageGroupSSlavic.getUuid().toString());
    languageGroupSSlavic.addAttribute(AttributeString.builder()
      .name("prop1").value("prop1 value").build());
    languageGroupSSlavic.addAttribute(AttributeBool.builder()
      .name("prop2").value(Boolean.FALSE).build());
    languageGroupSSlavic.addAttribute(AttributeDate.builder()
      .name("prop3").value(Iso8601.fromIso("2022-08-01")).build());
    languageGroupSSlavic.addAttribute(AttributeValueList.builder()
      .name("prop4").value(List.of("item1", "item2", "item3")).build());
    //languageGroupSSlavic.addTag(languageGroupSlavic);

    LanguageGroup languageGroupItalic = new LanguageGroup();
    languageGroupItalic.setName("italic");
    languageGroupItalic.setLang("en");
    languageGroupItalic.setTitle("Italic");
    assertEquals("42b9a7b1-8685-3209-bcd8-23b3b8463374", languageGroupItalic.getUuid().toString());
    languageGroupItalic.addAttribute(AttributeString.builder()
      .name("prop1").value("prop1 value").build());
    languageGroupItalic.addAttribute(AttributeBool.builder()
      .name("prop2").value(Boolean.FALSE).build());
    languageGroupItalic.addAttribute(AttributeDate.builder()
      .name("prop3").value(Iso8601.fromIso("2022-08-01")).build());
    languageGroupItalic.addAttribute(AttributeValueList.builder()
      .name("prop4").value(List.of(
        Iso8601.fromIso("2022-08-01"),
        Iso8601.fromIso("2022-08-02"),
        Iso8601.fromIso("2022-08-03"))).build());

    List<LanguageGroup> result = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic(ADMIN_USER, TEST_PASSWORD)
      .and()
      .body(List.of(languageGroupSSlavic, languageGroupItalic))
      .when()
      .post(TAG_ENDPOINT)
      .then()
      .statusCode(200)
      //.log().all()
      .extract()
      .body().jsonPath().getList("data", LanguageGroup.class);
    assertEquals(2, result.size());
    /*Role respRole = result.get(0);
    assertEquals(role, respRole);
    assertEquals(role.getTitle(), respRole.getTitle());
    assertEquals(role.getLang(), respRole.getLang());
    assertEquals(role.getTranslations(), respRole.getTranslations());*/
  }

  @Test
  @Order(30)
  @Tag("TagResourceTest.removeAttribute")
  public void removeAttribute() {
    LanguageGroup languageGroupSSlavic = new LanguageGroup();
    languageGroupSSlavic.setName("southern_slavic");
    assertEquals("0fe038f8-6f6f-3b71-97db-4d00b7a0ca9f", languageGroupSSlavic.getUuid().toString());
    languageGroupSSlavic.addAttribute(new AttributeToRemove("prop1"));
    languageGroupSSlavic.addAttribute(AttributeBool.builder()
        .name("prop2").value(Boolean.FALSE).build());
    languageGroupSSlavic.addAttribute(AttributeDate.builder()
        .name("prop3").value(Iso8601.fromIso("2022-08-01")).build());
    languageGroupSSlavic.addAttribute(AttributeValueList.builder()
        .name("prop4").value(List.of("item1", "item2", "item3")).build());
    //languageGroupSSlavic.addTag(languageGroupSlavic);

    List<LanguageGroup> result = given()
        //.log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
        //.header("Authorization", "Bearer editortoken1")
        .auth().preemptive().basic(ADMIN_USER, TEST_PASSWORD)
        .and()
        .body(List.of(languageGroupSSlavic))
        .when()
        .put(TAG_ENDPOINT)
        .then()
        .statusCode(200)
        //.log().all()
        .extract()
        .body().jsonPath().getList("data", LanguageGroup.class);
    assertEquals(1, result.size());
    assertEquals(3, result.getFirst().getAttributes().size());
  }
}
