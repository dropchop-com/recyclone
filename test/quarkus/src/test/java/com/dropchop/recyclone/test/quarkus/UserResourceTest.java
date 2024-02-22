package com.dropchop.recyclone.test.quarkus;

import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 25. 05. 22.
 */
//@Disabled
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {


  private static final String userId = UUID.randomUUID().toString();

  @Test
  @Order(10)
  public void create() {
    User user = new User();
    user.setId(userId);
    user.setLanguage(new Language("en"));
    user.setFirstName("test");
    user.setLastName("test");
    user.setCreated(ZonedDateTime.now());
    user.setModified(ZonedDateTime.now());

    List<User> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(user))
      .when()
      .post("/api/internal/security/user")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", User.class);
    assertEquals(1, result.size());
    User respUser = result.get(0);
    assertEquals(user, respUser );
    assertEquals(user.getLanguage(), respUser.getLanguage());
    assertEquals(user.getCountry(), respUser.getCountry());
    assertEquals(user.getFirstName(), respUser.getFirstName());
    assertEquals(user.getLastName(), respUser.getLastName());

  }


  @Test
  @Order(20)
  public void update() {
    User user = new User();
    user.setId(userId);
    user.setLanguage(new Language("en"));
    user.setFirstName("test update");
    user.setLastName("test update");
    user.setModified(ZonedDateTime.now());

    List<User> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(user))
      .when()
      .put("/api/internal/security/user")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", User.class);
    assertEquals(1, result.size());
    User respUser = result.get(0);
    assertEquals(user, respUser );
    assertEquals(user.getLanguage(), respUser.getLanguage());
    assertEquals(user.getCountry(), respUser.getCountry());
    assertEquals(user.getFirstName(), respUser.getFirstName());
    assertEquals(user.getLastName(), respUser.getLastName());
  }

}
