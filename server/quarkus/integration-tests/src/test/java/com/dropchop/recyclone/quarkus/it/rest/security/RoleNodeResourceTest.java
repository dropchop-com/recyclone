package com.dropchop.recyclone.quarkus.it.rest.security;


import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.invoke.RoleNodePermissionParams;
import com.dropchop.recyclone.model.dto.security.*;
import com.dropchop.recyclone.rest.api.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.rest.Constants.Paths.INTERNAL_SEGMENT;
import static com.dropchop.recyclone.model.api.rest.Constants.Paths.SEARCH_SEGMENT;
import static com.dropchop.recyclone.model.api.rest.Constants.Paths.Security.*;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.junit.jupiter.api.Assertions.*;

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


  private static final String UUID3 = UUID.randomUUID().toString();
  private static final String TPL_TARGET_NAME = "TPL_FOR_SOME_TARGET";

  private static final String DOMAIN_ACCOUNT = "accounting.account";

  private static final String PERMISSION1 = UUID.randomUUID().toString();
  private static final String PERMISSION2 = UUID.randomUUID().toString();
  private static final String PERMISSION3 = UUID.randomUUID().toString();
  private static final String PERMISSION4 = UUID.randomUUID().toString();

  @Inject
  ObjectMapper mapper;

  @BeforeEach
  public void setUp() {
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
        objectMapperConfig().jackson2ObjectMapperFactory((type, s) -> mapper
        ));
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



  private List<Permission> getPermissions() {
    Permission permissionView = SecurityHelper.permissionOf(PERMISSION3, DOMAIN_ACCOUNT, Constants.Actions.VIEW);
    Permission permissionCreate = SecurityHelper.permissionOf(PERMISSION1, DOMAIN_ACCOUNT, Constants.Actions.CREATE);
    Permission permissionUpdate = SecurityHelper.permissionOf(PERMISSION2, DOMAIN_ACCOUNT, Constants.Actions.UPDATE);
    Permission permissionDelete = SecurityHelper.permissionOf(PERMISSION4, DOMAIN_ACCOUNT, Constants.Actions.DELETE);
    return List.of(permissionView, permissionCreate, permissionUpdate, permissionDelete);
  }


  private List<Permission> prepPermissions() {

    List<Permission> permissions = this.getPermissions();

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
    RoleNode roleNode = SecurityHelper.roleNodeOf(UUID1, ENTITY1, ENTITY1_ID, null);
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
    assertEquals(1, result.size());
    return result.get(0);
  }


  private  <P extends RoleNodePermission> List<P> prepRoleNodePermissions(RoleNode node,
                                                                          List<Permission> permissions,
                                                                          String target,
                                                                          String targetId,
                                                                          boolean asTemplate) {
    return (List<P>) permissions.stream()
      .map(p -> SecurityHelper.roleNodePermissionOf(UUID.randomUUID().toString(), p, target, targetId, true, node, asTemplate))
      .toList();
  }


  @Test
  @Order(10)
  public void createRoleNodeForEntity() {
    RoleNode roleNode = SecurityHelper.roleNodeOf(UUID1, ENTITY1, ENTITY1_ID, null);
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

    roleNode = SecurityHelper.roleNodeOf(UUID2, ENTITY2, ENTITY2_ID, null);
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

  @Test
  @Order(40)
  public void addPermissionToRoleNode() {

    this.prepDomain();
    List<Permission> permissions = this.prepPermissions();
    RoleNode roleNode1 = this.prepRoleNode1();
    RoleNode roleNode2 = this.getRoleNode2();

    List<RoleNodePermission> roleNodePermissions1 = this.prepRoleNodePermissions(roleNode1, permissions, null, null, false);
    List<RoleNodePermission> roleNodePermissions2 = this.prepRoleNodePermissions(roleNode2, List.of(permissions.get(0)),  null, null,false);


    //store all permissions on role node
    List<RoleNodePermission> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(roleNodePermissions1)
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE_PERMISSION)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNodePermission.class);
    assertEquals(4, result.size());


    //store one permissions on role node
    result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(roleNodePermissions2)
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE_PERMISSION)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNodePermission.class);
    assertEquals(1, result.size());


  }


  @Test
  @Order(50)
  public void getRoleNodePermissions() {

    RoleNodePermissionParams params = new RoleNodePermissionParams();
    params.setRoleNodeId(UUID1);

    List<RoleNodePermission> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(params)
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE_PERMISSION + SEARCH_SEGMENT)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNodePermission.class);
    assertEquals(4, result.size());

    params.setRoleNodeId(UUID2);

    result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(params)
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE_PERMISSION + SEARCH_SEGMENT)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNodePermission.class);
    assertEquals(1, result.size());
  }


  @Test
  @Order(60)
  public void addTemplatePermissionToRoleNode() {

    RoleNode roleNodeTpl = SecurityHelper.roleNodeOf(UUID3, TPL_TARGET_NAME, null, null, null, null);
    List<RoleNode> result = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(roleNodeTpl))
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    assertEquals(1, result.size());
    RoleNode respRole = result.get(0);
    assertEquals(roleNodeTpl, respRole);
    assertEquals(roleNodeTpl.getUuid(), respRole.getUuid());
    assertEquals(roleNodeTpl.getTarget(), respRole.getTarget());
    assertEquals(roleNodeTpl.getEntity(), respRole.getEntity());

    List<RoleNodePermissionTemplate> roleNodePermissions1 = this.prepRoleNodePermissions(roleNodeTpl,
      this.getPermissions(), TPL_TARGET_NAME, null, true);

    //store all permissions on role node
    List<RoleNodePermissionTemplate> tplPermissionResult = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(roleNodePermissions1)
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE_PERMISSION)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNodePermissionTemplate.class);
    assertEquals(4, tplPermissionResult.size());
    for (RoleNodePermission p : tplPermissionResult) {
      assertInstanceOf(RoleNodePermissionTemplate.class, p);
    }

    List<RoleNode> resultTplNode = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .get("/api" + INTERNAL_SEGMENT + ROLE_NODE + "/" + UUID3 + "?c_level=5")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    RoleNode tplNode = resultTplNode.get(0);
    for (RoleNodePermission p : tplNode.getRoleNodePermissions()) {
      assertInstanceOf(RoleNodePermissionTemplate.class, p);
      assertNotNull(((RoleNodePermissionTemplate)p).getTarget());
    }
  }

}




