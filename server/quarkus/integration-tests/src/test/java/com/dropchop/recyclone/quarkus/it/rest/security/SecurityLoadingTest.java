package com.dropchop.recyclone.quarkus.it.rest.security;


import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.security.*;
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
import static com.dropchop.recyclone.quarkus.it.rest.security.SecurityLoadingTest.Data.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecurityLoadingTest {

  interface Data {

    //security entities
    String DOMAIN_PROJECTS = "organization.projects";

    String PERMISSION1 = UUID.randomUUID().toString();
    String PERMISSION2 = UUID.randomUUID().toString();
    String PERMISSION3 = UUID.randomUUID().toString();
    String PERMISSION4 = UUID.randomUUID().toString();


    //entity user
    String USER_ENTITY = "user";
    String USER_ENTITY_ID = UUID.randomUUID().toString();
    String USER_ROLE_NODE_ID = UUID.randomUUID().toString();

    //entity organization
    String ORG_ENTITY = "org";
    String ORG_ENTITY_ID = UUID.randomUUID().toString();
    String ORG_ROLE_NODE_ID = UUID.randomUUID().toString();

    //entity organization unit
    String ORG_UNIT_ENTITY = "org_unit";
    String ORG_UNIT_ENTITY_ID = UUID.randomUUID().toString();
    String ORG_UNIT_ROLE_NODE_ID = UUID.randomUUID().toString();


    //template organization
    String ORG_TEMPLATE_ROLE_NODE_ID = UUID.randomUUID().toString();

    //template organization unit
    String ORG_UNIT_TEMPLATE_ROLE_NODE_ID = UUID.randomUUID().toString();

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
    domain.setCode(DOMAIN_PROJECTS);
    domain.setLang("en");
    domain.setTitle(DOMAIN_PROJECTS);
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

    Permission permissionView = SecurityHelper.permissionOf(PERMISSION1, DOMAIN_PROJECTS, Constants.Actions.VIEW);
    Permission permissionCreate = SecurityHelper.permissionOf(PERMISSION2, DOMAIN_PROJECTS, Constants.Actions.CREATE);
    Permission permissionUpdate = SecurityHelper.permissionOf(PERMISSION3, DOMAIN_PROJECTS, Constants.Actions.UPDATE);
    Permission permissionDelete = SecurityHelper.permissionOf(PERMISSION4, DOMAIN_PROJECTS, Constants.Actions.DELETE);

    List<Permission> permissions = List.of(permissionView, permissionCreate, permissionUpdate, permissionDelete);

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


  private List<RoleNodePermission> prepRoleNodePermissions(RoleNode node, List<Permission> permissions, boolean asTemplate) {
    return permissions.stream()
      .map(p -> SecurityHelper.roleNodePermissionOf(UUID.randomUUID().toString(), p, true, node, asTemplate))
      .toList();
  }


  @Test
  @Order(10)
  public void testLoadTemplatePermissions() {

    this.prepDomain();
    List<Permission> permissions = this.prepPermissions();

    RoleNode roleNodeOrgTemplate = SecurityHelper.roleNodeOf(ORG_TEMPLATE_ROLE_NODE_ID, ORG_ENTITY, null, null,null);
    RoleNode roleNodeOrgUnitTemplate = SecurityHelper.roleNodeOf(ORG_UNIT_TEMPLATE_ROLE_NODE_ID, ORG_UNIT_ENTITY, null, null,null);

    List<RoleNodePermission> roleNodeOrgPermissions = this.prepRoleNodePermissions(roleNodeOrgTemplate, permissions, true);
    List<RoleNodePermission> roleNodeOrgUnitPermissions = this.prepRoleNodePermissions(roleNodeOrgUnitTemplate, permissions, true);

    //store templates for org role node
    List<RoleNode> resultOrg = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(roleNodeOrgTemplate))
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    assertEquals(1, resultOrg.size());
    RoleNode respRoleOrg = resultOrg.get(0);
    assertEquals(roleNodeOrgTemplate, respRoleOrg);
    assertEquals(roleNodeOrgTemplate.getUuid(), respRoleOrg.getUuid());
    assertEquals(roleNodeOrgTemplate.getEntity(), respRoleOrg.getEntity());
    assertEquals(roleNodeOrgTemplate.getEntityId(), respRoleOrg.getEntityId());

    List<RoleNodePermission> resultOrgPermissions = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(roleNodeOrgPermissions)
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE_PERMISSION)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNodePermission.class);
    assertEquals(4, resultOrgPermissions.size());

    //store templates for org unit role node
    List<RoleNode> resultOrgUnit = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(roleNodeOrgUnitTemplate))
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNode.class);
    assertEquals(1, resultOrgUnit.size());
    RoleNode respOrgUnit = resultOrgUnit.get(0);
    assertEquals(roleNodeOrgUnitTemplate, respOrgUnit);
    assertEquals(roleNodeOrgUnitTemplate.getUuid(), respOrgUnit.getUuid());
    assertEquals(roleNodeOrgUnitTemplate.getEntity(), respOrgUnit.getEntity());
    assertEquals(roleNodeOrgUnitTemplate.getEntityId(), respOrgUnit.getEntityId());

    List<RoleNodePermission> resultOrgUnitPermissions = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(roleNodeOrgUnitPermissions)
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE_PERMISSION)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNodePermission.class);
    assertEquals(4, resultOrgUnitPermissions.size());

    //
    //load template permissions and check if permissions are set properly
    //

    RoleNodeParams orgParams = new RoleNodeParams();
    orgParams.setTarget(ORG_ENTITY);

    List<RoleNodePermission> loadedOrgTemplatePermissions = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(orgParams)
      .when()
      .post("/api" + INTERNAL_SEGMENT + PERMISSIONS + PERMISSIONS_LIST_SEGMENT)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNodePermission.class);
    assertEquals(4, loadedOrgTemplatePermissions.size());

    RoleNodeParams orgUnitParams = new RoleNodeParams();
    orgUnitParams.setTarget(ORG_UNIT_ENTITY);

    List<RoleNodePermission> loadedOrgUnitTemplatePermissions = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(orgUnitParams)
      .when()
      .post("/api" + INTERNAL_SEGMENT + PERMISSIONS + PERMISSIONS_LIST_SEGMENT)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNodePermission.class);
    assertEquals(4, loadedOrgUnitTemplatePermissions.size());


  }



}
