package com.dropchop.recyclone.quarkus.it.rest.security;

import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.base.dto.model.security.LoginAccount;
import com.dropchop.recyclone.base.dto.model.security.TokenAccount;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 25. 05. 22.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

  private static final String userId = UUID.randomUUID().toString();
  private static final String userId2 = UUID.randomUUID().toString();

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
    User respUser = result.getFirst();
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
    User respUser = result.getFirst();
    assertEquals(user, respUser );
    assertEquals(user.getLanguage(), respUser.getLanguage());
    assertEquals(user.getCountry(), respUser.getCountry());
    assertEquals(user.getFirstName(), respUser.getFirstName());
    assertEquals(user.getLastName(), respUser.getLastName());
  }

  //@Test
  @Order(30)
  @SuppressWarnings("unused")
  public void createUserWithAccounts() {
    User user = new User();
    user.setId(userId2);
    user.setLanguage(new Language("en"));
    user.setFirstName("test with accounts");
    user.setLastName("test with accounts");
    user.setModified(ZonedDateTime.now());

    String loginAccountId = UUID.randomUUID().toString();
    String tokenAccountId = UUID.randomUUID().toString();
    String token = UUID.randomUUID().toString();

    LoginAccount loginAccount = new LoginAccount();
    loginAccount.setId(loginAccountId);
    loginAccount.setModified(ZonedDateTime.now());
    loginAccount.setCreated(ZonedDateTime.now());
    loginAccount.setLoginName("test");
    loginAccount.setPassword("test");

    TokenAccount tokenAccount = new TokenAccount();
    tokenAccount.setId(tokenAccountId);
    tokenAccount.setModified(ZonedDateTime.now());
    tokenAccount.setCreated(ZonedDateTime.now());
    tokenAccount.setToken(token);

    user.setAccounts(Set.of(loginAccount, tokenAccount));

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
    User respUser = result.getFirst();
    assertEquals(user, respUser );
    assertEquals(user.getLanguage(), respUser.getLanguage());
    assertEquals(user.getCountry(), respUser.getCountry());
    assertEquals(user.getFirstName(), respUser.getFirstName());
    assertEquals(user.getLastName(), respUser.getLastName());
    assertEquals(user.getAccounts().size(), respUser.getAccounts().size());
  }
}
