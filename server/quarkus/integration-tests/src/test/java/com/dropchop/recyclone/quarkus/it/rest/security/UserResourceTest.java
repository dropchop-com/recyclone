package com.dropchop.recyclone.quarkus.it.rest.security;

import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.base.dto.model.security.LoginAccount;
import com.dropchop.recyclone.base.dto.model.security.TokenAccount;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 25. 05. 22.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

  private static final String userId = UUID.randomUUID().toString();
  private static final String userId2 = UUID.randomUUID().toString();
  private static final String loginAccountId = UUID.randomUUID().toString();
  private static final String tokenAccountId = UUID.randomUUID().toString();

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
  @Tag("create")
  @Tag("update")
  @Tag("createUserWithAccounts")
  @Tag("getUserById")
  @Tag("deleteUserAccount")
  public void create() {
    User user = new User();
    user.setId(userId);
    user.setLanguage(new Language("en"));
    user.setFirstName("test");
    user.setLastName("test");
    user.setCreated(ZonedDateTime.now());
    user.setModified(ZonedDateTime.now());

    User user2 = new User();
    user2.setId(userId2);
    user2.setLanguage(new Language("en"));
    user2.setFirstName("test with accounts");
    user2.setLastName("test with accounts");
    user2.setModified(ZonedDateTime.now());


    List<User> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(user,user2))
      .when()
      .post("/api/internal/security/user")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", User.class);
    assertEquals(2, result.size());
    User respUser = result.stream().filter((User u) -> u.getId().equals(userId)).findFirst().orElse(null);
    assertNotNull(respUser);
    assertEquals(user, respUser );
    assertEquals(user.getLanguage(), respUser.getLanguage());
    assertEquals(user.getCountry(), respUser.getCountry());
    assertEquals(user.getFirstName(), respUser.getFirstName());
    assertEquals(user.getLastName(), respUser.getLastName());
  }

  @Test
  @Order(20)
  @Tag("update")
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

  @Test
  @Order(30)
  @Tag("getUserById")
  public void getUserById() {
    List<User> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .when()
      .get("/api/internal/security/user/" + userId.toString())
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", User.class);
    assertEquals(1, result.size());
  }

  @Test
  @Order(40)
  @SuppressWarnings("unused")
  @Tag("createAccounts")
  @Tag("updateAccount")
  @Tag("deleteAccount")
  public void createAccounts() {
    User user2 = new User();
    user2.setId(userId2);

    String token = UUID.randomUUID().toString();

    ZonedDateTime deactivated = ZonedDateTime.of(1900,1,2, 0,0,0,0, ZoneId.systemDefault());

    LoginAccount loginAccount = new LoginAccount();
    loginAccount.setId(loginAccountId);
    loginAccount.setUser(user2);
    loginAccount.setModified(ZonedDateTime.now());
    loginAccount.setCreated(ZonedDateTime.now());
    loginAccount.setLoginName("test");
    loginAccount.setPassword("test");

    TokenAccount tokenAccount = new TokenAccount();
    tokenAccount.setId(tokenAccountId);
    tokenAccount.setUser(user2);
    tokenAccount.setToken(token);
    tokenAccount.setModified(ZonedDateTime.now());
    tokenAccount.setCreated(ZonedDateTime.now());
    tokenAccount.setDeactivated(deactivated);


    List<UserAccount> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(tokenAccount, loginAccount))
      .when()
      .post("/api/internal/security/user/accounts?c_level=3")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", UserAccount.class);
    assertEquals(2, result.size());

    List<User> userResult = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .when()
      .get("/api/internal/security/user/" + userId2 + "?c_level=5")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", User.class);
    assertEquals(1, userResult.size());
    User respUser = userResult.getFirst();
    assertEquals(2, respUser.getAccounts().size());

    respUser.getAccounts().forEach(account -> {
      if (account instanceof LoginAccount loginAccount1) {
        assertEquals(loginAccount.getLoginName(), loginAccount1.getLoginName());
        assertEquals(loginAccount.getPassword(), loginAccount1.getPassword());
      }
      if (account instanceof TokenAccount tokenAccount1) {
        assertEquals(tokenAccount.getToken(), tokenAccount1.getToken());
        assertEquals(tokenAccount.getDeactivated(), tokenAccount1.getDeactivated().withZoneSameInstant(ZoneId.systemDefault()));
      }
    });
  }


  @Test
  @Order(50)
  @Tag("updateAccount")
  public void updateAccounts() {
    User user2 = new User();
    user2.setId(userId2);
    String randomString = UUID.randomUUID().toString();

    ZonedDateTime deactivated = ZonedDateTime.of(1900,1,2, 0,0,0,0, ZoneId.systemDefault());

    TokenAccount tokenAccount = new TokenAccount();
    tokenAccount.setUser(user2);
    tokenAccount.setId(tokenAccountId);
    tokenAccount.setToken(randomString);
    tokenAccount.setDeactivated(deactivated);

    LoginAccount loginAccount = new LoginAccount();
    loginAccount.setUser(user2);
    loginAccount.setId(loginAccountId);
    loginAccount.setLoginName(randomString);
    loginAccount.setPassword(randomString);

    List<UserAccount> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(tokenAccount, loginAccount))
      .when()
      .put("/api/internal/security/user/accounts?c_level=5")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", UserAccount.class);

    List<User> userResult = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .when()
      .get("/api/internal/security/user/" + userId2 + "?c_level=5")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", User.class);
    assertEquals(1, userResult.size());
    User respUser = userResult.getFirst();
    assertEquals(2, respUser.getAccounts().size());

    respUser.getAccounts().forEach(account -> {
      if (account instanceof LoginAccount loginAccount1) {
        assertEquals(loginAccount.getLoginName(), loginAccount1.getLoginName());
        assertEquals(loginAccount.getPassword(), loginAccount1.getPassword());
      }
      if (account instanceof TokenAccount tokenAccount1) {
        assertEquals(tokenAccount.getToken(), tokenAccount1.getToken());
        assertEquals(tokenAccount.getDeactivated(), tokenAccount1.getDeactivated().withZoneSameInstant(ZoneId.systemDefault()));
      }
    });

  }

  @Test
  @Order(60)
  @Tag("deleteAccount")
  public void deleteAccount() {
    List<User> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .when()
      .get("/api/internal/security/user/" + userId2+ "?c_level=5")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", User.class);
    assertEquals(1, result.size());
    User respUser = result.getFirst();
    assertEquals(2, respUser.getAccounts().size());

    TokenAccount tokenAccount = new TokenAccount();
    tokenAccount.setId(respUser.getAccounts().stream().filter(account -> account instanceof TokenAccount).findFirst().get().getId());

    List<User> resultDelete = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(tokenAccount))
      .when()
      .delete("/api/internal/security/user/accounts")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", User.class);

    result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .when()
      .get("/api/internal/security/user/" + userId2+ "?c_level=5")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", User.class);
    assertEquals(1, result.size());
    respUser = result.getFirst();
    assertEquals(1, respUser.getAccounts().size());
    assertEquals(LoginAccount.class, respUser.getAccounts().iterator().next().getClass());
  }

}
