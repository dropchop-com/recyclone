package com.dropchop.recyclone.quarkus.it.rest.security;

import com.dropchop.recyclone.base.dto.model.base.DtoId;
import com.dropchop.recyclone.base.dto.model.invoke.RoleParams;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.dto.model.security.Role;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.dropchop.recyclone.quarkus.it.rest.Constants.ROLE_ENDPOINT;
import static com.dropchop.recyclone.quarkus.it.rest.Constants.ROLE_PERM_ENDPOINT;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 25. 05. 22.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleResourceTest {

  @Test
  @Order(10)
  public void createErrorMissingLang() {
    Role role = new Role();
    role.setCode("test_role");

    given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(role))
      .when()
      .post(ROLE_ENDPOINT)
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
    Role role = new Role();
    role.setCode("test_role");
    role.setLang("en");
    role.setTitle("Test");
    role.addTranslation(new TitleDescriptionTranslation("sl", "Test"));

    List<Role> result = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(role))
      .when()
      .post(ROLE_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", Role.class);
    assertEquals(1, result.size());
    Role respRole = result.getFirst();
    assertEquals(role, respRole);
    assertEquals(role.getTitle(), respRole.getTitle());
    assertEquals(role.getLang(), respRole.getLang());
    assertEquals(role.getTranslations(), respRole.getTranslations());
  }

  @Test
  @Order(25)
  public void update() {
    Role role = new Role();
    role.setCode("test_role");
    role.setLang("en");
    role.setTitle("Test update ");
    role.addTranslation(new TitleDescriptionTranslation("sl", "Test update "));

    List<Role> result = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(role))
      .when()
      .put(ROLE_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", Role.class);
    assertEquals(1, result.size());
    Role respRole = result.getFirst();
    assertEquals(role, respRole);
    assertEquals(role.getTitle(), respRole.getTitle());
    assertEquals(role.getLang(), respRole.getLang());
    assertEquals(role.getTranslations(), respRole.getTranslations());
  }



  @Test
  @Order(30)
  public void addPermissions() {
    RoleParams params = new RoleParams();
    params.code("test_role");
    params.filter().content().treeLevel(2);
    List<UUID> permissions = List.of(
      UUID.fromString("f321134e-d383-45c7-bbbf-befc44f41d0c"),
      UUID.fromString("0edf37b1-8b7c-4d81-9f17-05f683f54570"),
      UUID.fromString("9629c163-de68-4c6d-8bd5-9bdb5277bcbd"),
      UUID.fromString("83ea8f0e-5261-445d-beb5-c40f35d1fe4e"),
      UUID.fromString("0c4c4ed4-af82-4e3f-b244-f530e5f8b9b1")
    );
    params.setPermissionUuids(permissions);

    List<Role> roles = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(params)
      .when()
      .put(ROLE_PERM_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Role.class);
    assertEquals(1, roles.size());
    Role respRole = roles.getFirst();
    Set<String> perms = permissions.stream().map(UUID::toString).collect(Collectors.toSet());
    Set<String> respPerms = respRole.getPermissions().stream().map(DtoId::getId).collect(Collectors.toSet());
    assertEquals(perms, respPerms);
  }

  @Test
  @Order(40)
  public void removeSomePermissions() {
    RoleParams params = new RoleParams();
    params.code("test_role");
    params.filter().content().treeLevel(2);
    List<UUID> permissions = List.of(
      UUID.fromString("9629c163-de68-4c6d-8bd5-9bdb5277bcbd"),
      UUID.fromString("83ea8f0e-5261-445d-beb5-c40f35d1fe4e"),
      UUID.fromString("0c4c4ed4-af82-4e3f-b244-f530e5f8b9b1")
    );
    params.setPermissionUuids(permissions);

    permissions = List.of(
      UUID.fromString("f321134e-d383-45c7-bbbf-befc44f41d0c"),
      UUID.fromString("0edf37b1-8b7c-4d81-9f17-05f683f54570")
    );
    List<Role> roles = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(params)
      .when()
      .delete(ROLE_PERM_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Role.class);
    assertEquals(1, roles.size());
    Role respRole = roles.getFirst();
    Set<String> perms = permissions.stream().map(UUID::toString).collect(Collectors.toSet());
    Set<String> respPerms = respRole.getPermissions().stream().map(DtoId::getId).collect(Collectors.toSet());
    //TODO: FIX TESTS CAUSE IT'S NO LONGER ENOUGH TO POST PERMISSION UUIDS !!!!
    assertEquals(perms, respPerms);
  }

  @Test
  @Order(50)
  public void removeOtherPermissions() {
    RoleParams params = new RoleParams();
    params.code("test_role");
    params.filter().content().treeLevel(2);
    List<UUID> permissions = List.of(
      UUID.fromString("f321134e-d383-45c7-bbbf-befc44f41d0c"),
      UUID.fromString("0edf37b1-8b7c-4d81-9f17-05f683f54570")
    );
    params.setPermissionUuids(permissions);

    List<Role> roles = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(params)
      .when()
      .delete(ROLE_PERM_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Role.class);
    //TODO: FIX TESTS CAUSE IT'S NO LONGER ENOUGH TO POST PERMISSION UUIDS !!!!
    assertEquals(1, roles.size());
    Role respRole = roles.getFirst();
    assertEquals(0, respRole.getPermissions().size());
  }

  @Test
  @Order(60)
  public void delete() {
    Role role = new Role();
    role.setCode("test_role");
    List<Role> roles = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(role))
      .when()
      .delete(ROLE_ENDPOINT)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", Role.class);
    assertEquals(1, roles.size());
  }
}
