package com.dropchop.recyclone.quarkus.it.rest.security;


import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.rest.api.MediaType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.rest.Constants.Paths.INTERNAL_SEGMENT;
import static com.dropchop.recyclone.model.api.rest.Constants.Paths.Security.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for RoleNode CRUD operations.
 */

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleNodeResourceTest {


  private static final String UUID1 = UUID.randomUUID().toString();
  private static final String ENTITY1 = "Role";
  private static final String ENTITY1_ID = "accountant";

  private static final String UUID2 = UUID.randomUUID().toString();
  private static final String ENTITY2 = "Role";
  private static final String ENTITY2_ID = "assistant";

  private static final String DOMAIN_ACCOUNT = "accounting.account";

  private static final String PERMISSION1 = UUID.randomUUID().toString();
  private static final String PERMISSION2 = UUID.randomUUID().toString();
  private static final String PERMISSION3 = UUID.randomUUID().toString();
  private static final String PERMISSION4 = UUID.randomUUID().toString();


  public static RoleNode roleNodeOf(String uuid, String entity, String entityUuid) {
    RoleNode roleNode = new RoleNode();
    roleNode.setUuid(uuid);
    roleNode.setTarget(entity);
    roleNode.setEntity(entity);
    roleNode.setEntityId(entityUuid);
    return roleNode;
  }



  public static Permission permissionOf(String uuid, String domainCode, String actionCode) {
    Domain domain = new Domain();
    domain.setCode(domainCode);

    Action action = new Action();
    action.setCode(actionCode);

    Permission permission = new Permission();
    permission.setUuid(uuid);
    permission.setDomain(domain);
    permission.setAction(action);
    permission.setTitle("Permit " + actionCode + " actions on " + domainCode);
    permission.setLang("en");
    return permission;
  }



  @Test
  @Order(10)
  public void createRoleNodeForEntity() {
    RoleNode roleNode = roleNodeOf(UUID1, ENTITY1, ENTITY1_ID);
    List<RoleNode> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(roleNode))
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    assertEquals(1, result.size());
    RoleNode respRole = result.get(0);
    assertEquals(roleNode, respRole);
    assertEquals(roleNode.getUuid(), respRole.getUuid());
    assertEquals(roleNode.getEntity(), respRole.getEntity());
    assertEquals(roleNode.getEntityId(), respRole.getEntityId());

    roleNode = roleNodeOf(UUID2, ENTITY2, ENTITY2_ID);
    result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(roleNode))
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    assertEquals(1, result.size());
    respRole = result.get(0);
    assertEquals(roleNode, respRole);
    assertEquals(roleNode.getUuid(), respRole.getUuid());
    assertEquals(roleNode.getEntity(), respRole.getEntity());
    assertEquals(roleNode.getEntityId(), respRole.getEntityId());
  }

  @Test
  @Order(20)
  public void updateEntityRoleNode() {
    List<RoleNode> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .get("/api" + INTERNAL_SEGMENT + ROLE_NODE + "/" + UUID1)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    RoleNode respRole = result.get(0);
    assertEquals(UUID1, respRole.getUuid().toString());
    assertEquals(ENTITY1, respRole.getTarget());
    assertEquals(ENTITY1, respRole.getEntity());
    assertEquals(ENTITY1_ID, respRole.getEntityId());

    respRole.setTarget(ENTITY2);
    respRole.setEntity(ENTITY2);
    respRole.setEntityId(ENTITY2_ID);

    result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(respRole))
      .when()
      .put("/api" + INTERNAL_SEGMENT + ROLE_NODE)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    assertEquals(1, result.size());
    RoleNode respRoleUpdated = result.get(0);
    assertEquals(respRole.getUuid(), respRoleUpdated.getUuid());
    assertEquals(respRole.getTarget(), respRoleUpdated.getTarget());
    assertEquals(respRole.getEntity(), respRoleUpdated.getEntity());
    assertEquals(respRole.getEntityId(), respRoleUpdated.getEntityId());

    result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .get("/api" + INTERNAL_SEGMENT + ROLE_NODE + "/" + UUID1)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    respRole = result.get(0);
    assertEquals(UUID1, respRole.getUuid().toString());
    assertEquals(ENTITY2, respRole.getTarget());
    assertEquals(ENTITY2, respRole.getEntity());
    assertEquals(ENTITY2_ID, respRole.getEntityId());
  }


  @Test
  @Order(30)
  public void deleteEntityRoleNode() {

    RoleNode node = new RoleNode();
    node.setUuid(UUID1);

    given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(node))
      .when()
      .delete("/api" + INTERNAL_SEGMENT + ROLE_NODE)
      .then()
      .statusCode(200);


    List<RoleNode> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .get("/api" + INTERNAL_SEGMENT + ROLE_NODE + "/" + UUID1)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    assertTrue(result.isEmpty());
  }


  private Domain prepDomain() {

    Action view = new Action();
    view.setCode(Constants.Actions.VIEW);

    Action create = new Action();;
    create.setCode(Constants.Actions.CREATE);

    Action update = new Action();;
    update.setCode(Constants.Actions.UPDATE);

    Action delete = new Action();;
    delete.setCode(Constants.Actions.DELETE);

    Domain domain = new Domain();
    domain.setCode(DOMAIN_ACCOUNT);
    domain.setLang("en");
    domain.setTitle(DOMAIN_ACCOUNT);
    domain.setActions(Set.of(view, create, update, delete));

    List<Domain> domainsResult = given()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(domain))
      .when()
      .post("/api" + INTERNAL_SEGMENT + DOMAIN)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Domain.class);
    assertEquals(1, domainsResult.size());
    Domain resultDomain = domainsResult.get(0);
    assertEquals(4, resultDomain.getActions().size());

    return resultDomain;
  }


  private List<Permission> prepPermissions() {

    Permission permissionCreate = permissionOf(PERMISSION1, DOMAIN_ACCOUNT, Constants.Actions.CREATE);
    Permission permissionUpdate = permissionOf(PERMISSION2, DOMAIN_ACCOUNT, Constants.Actions.UPDATE);
    Permission permissionView = permissionOf(PERMISSION3, DOMAIN_ACCOUNT, Constants.Actions.VIEW);
    Permission permissionDelete = permissionOf(PERMISSION4, DOMAIN_ACCOUNT, Constants.Actions.DELETE);

    List<Permission> permissions = List.of(permissionCreate, permissionUpdate, permissionView, permissionDelete);

    List<Permission> permissionsResult = given()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(permissions)
      .when()
      .post("/api" + INTERNAL_SEGMENT + PERMISSION + "?c_level=2")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", Permission.class);
    assertEquals(permissions.size(), permissionsResult.size());

    return permissions;
  }


  private RoleNode prepRoleNode1() {
    RoleNode roleNode = roleNodeOf(UUID1, ENTITY1, ENTITY1_ID);
    List<RoleNode> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(roleNode))
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    assertEquals(1, result.size());
    return result.get(0);
  }


  private RoleNode getRoleNode2() {
    List<RoleNode> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .get("/api" + INTERNAL_SEGMENT + ROLE_NODE + "/" + UUID2)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    assertTrue(result.isEmpty());
    return result.get(0);
  }


  @Test
  @Order(40)
  public void addPermissionToRoleNode() {

    Domain domain = this.prepDomain();
    List<Permission> permissions = this.prepPermissions();
    RoleNode roleNode1 = this.prepRoleNode1();
    RoleNode roleNode2 = this.getRoleNode2();




/*

    List<RoleNode> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .get("/api" + INTERNAL_SEGMENT + ROLE_NODE + UUID1)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    RoleNode respRole = result.get(0);

*/


  }
}




