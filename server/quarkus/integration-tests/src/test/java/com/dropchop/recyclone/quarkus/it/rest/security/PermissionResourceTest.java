package com.dropchop.recyclone.quarkus.it.rest.security;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.dto.model.security.Action;
import com.dropchop.recyclone.base.dto.model.security.Domain;
import com.dropchop.recyclone.base.dto.model.security.Permission;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 25. 05. 22.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PermissionResourceTest {

  @Test
  @Order(10)
  public void create() {
    Domain domain = new Domain();
    domain.setCode(Constants.Domains.Localization.LANGUAGE);
    Action action = new Action();
    action.setCode(Constants.Actions.ALL);
    Permission permission = new Permission();
    permission.setUuid("28c9e87d-befe-4c70-b582-c176653d917c");
    permission.setAction(action);
    permission.setDomain(domain);
    permission.setTitle("Permit all actions on Language");
    permission.setLang("en");
    permission.addTranslation(new TitleDescriptionTranslation("sl", "Dovoli vse akcije na jezikih."));

    List<Permission> permissions = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(permission))
      .when()
      .post("/api/internal/security/permission?c_level=2")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Permission.class);
    assertEquals(1, permissions.size());
    Permission retPermission = permissions.getFirst();
    assertEquals(permission, retPermission);
    assertEquals(permission.getDomain(), retPermission.getDomain());
    assertEquals("Localization/Languages", retPermission.getDomain().getTitle());
    assertEquals("en", retPermission.getDomain().getLang());
    assertEquals(permission.getAction(), retPermission.getAction());
    assertEquals("All", retPermission.getAction().getTitle());
    assertEquals("en", retPermission.getAction().getLang());
    assertEquals("Permit all actions on Language", retPermission.getTitle());
    assertEquals("en", retPermission.getLang());
    assertEquals(
        Set.of(new TitleDescriptionTranslation("sl", "Dovoli vse akcije na jezikih.")),
        retPermission.getTranslations()
    );
  }

  //TODO: FIX THE PROBLEM WITH GETTING ENTITY BY UUID
  @Test
  @Order(20)
  //@Disabled
  public void get() {
    String permUuid = UUID.randomUUID().toString();
    Domain domain = new Domain();
    domain.setCode(Constants.Domains.Localization.LANGUAGE);
    Action action = new Action();
    action.setCode(Constants.Actions.ALL);
    Permission permission = new Permission();
    permission.setUuid(permUuid);
    permission.setAction(action);
    permission.setDomain(domain);
    permission.setTitle("Permit all actions on Language");
    permission.setLang("en");
    permission.addTranslation(new TitleDescriptionTranslation("sl", "Dovoli vse akcije na jezikih."));

    List<Permission> permissions = given()
        //.log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        //.header("Authorization", "Bearer editortoken1")
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(List.of(permission))
        .when()
        .post("/api/internal/security/permission?c_level=2")
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList(".", Permission.class);
    assertEquals(1, permissions.size());

    permissions = given()
        //.log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(List.of(permission))
        .when()
        .get("/api/internal/security/permission/" + permUuid + "?c_level=2")
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList(".", Permission.class);
    assertEquals(1, permissions.size());
    assertEquals(permUuid, permissions.getFirst().getUuid().toString());
  }

  @Test
  @Order(30)
  public void update() {
    Domain domain = new Domain();
    domain.setCode(Constants.Domains.Security.ROLE);
    Action action = new Action();
    action.setCode(Constants.Actions.UPDATE);
    Permission permission = new Permission();
    permission.setUuid("28c9e87d-befe-4c70-b582-c176653d917c");
    permission.setAction(action);
    permission.setDomain(domain);

    List<Permission> permissions = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(permission))
      .when()
      .put("/api/internal/security/permission?c_level=2")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Permission.class);
    assertEquals(1, permissions.size());
    Permission retPermission = permissions.getFirst();
    assertEquals(permission, retPermission);
    assertEquals(permission.getDomain(), retPermission.getDomain());
    assertEquals("Security/Roles", retPermission.getDomain().getTitle());
    assertEquals("en", retPermission.getDomain().getLang());
    assertEquals(permission.getAction(), retPermission.getAction());
    assertEquals("Update", retPermission.getAction().getTitle());
    assertEquals("en", retPermission.getAction().getLang());
    assertEquals("Permit all actions on Language", retPermission.getTitle());
    assertEquals("en", retPermission.getLang());
    assertEquals(
        Set.of(new TitleDescriptionTranslation("sl", "Dovoli vse akcije na jezikih.")),
        retPermission.getTranslations()
    );
  }

  @Test
  @Order(40)
  public void delete() {
    Permission permission = new Permission();
    permission.setUuid("28c9e87d-befe-4c70-b582-c176653d917c");

    List<Permission> permissions = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(permission))
      .when()
      .delete("/api/internal/security/permission?c_level=2")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Permission.class);
    assertEquals(1, permissions.size());
    Permission retPermission = permissions.getFirst();
    assertNotNull(retPermission);
    assertNotNull(retPermission.getDomain());
    assertEquals("Security/Roles", retPermission.getDomain().getTitle());
    assertEquals("en", retPermission.getDomain().getLang());
    assertNotNull(retPermission.getAction());
    assertEquals("Update", retPermission.getAction().getTitle());
    assertEquals("en", retPermission.getAction().getLang());
    assertEquals("Permit all actions on Language", retPermission.getTitle());
    assertEquals("en", retPermission.getLang());
    assertEquals(
        Set.of(new TitleDescriptionTranslation("sl", "Dovoli vse akcije na jezikih.")),
        retPermission.getTranslations()
    );
  }
}
