package com.dropchop.recyclone.quarkus.it.rest.security;


import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.invoke.RoleNodeParams;
import com.dropchop.recyclone.base.dto.model.security.*;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
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

import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.INTERNAL_SEGMENT;
import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.Security.*;
import static com.dropchop.recyclone.quarkus.it.rest.security.SecurityLoadingTest.Data.*;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.junit.jupiter.api.Assertions.*;

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

  @Inject
  ObjectMapper mapper;

  @BeforeEach
  public void setUp() {
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
        objectMapperConfig().jackson2ObjectMapperFactory((type, s) -> mapper
        ));
  }

  private static Domain getDomain() {
    Action view = new Action();
    view.setCode(Constants.Actions.VIEW);

    Action create = new Action();
    create.setCode(Constants.Actions.CREATE);

    Action update = new Action();
    update.setCode(Constants.Actions.UPDATE);

    Action delete = new Action();
    delete.setCode(Constants.Actions.DELETE);

    Domain domain = new Domain();
    domain.setCode(DOMAIN_PROJECTS);
    domain.setLang("en");
    domain.setTitle(DOMAIN_PROJECTS);
    domain.setActions(Set.of(view, create, update, delete));
    return domain;
  }

  private Domain prepDomain() {
    Domain domain = getDomain();

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
    Domain resultDomain = domainsResult.getFirst();
    assertEquals(4, resultDomain.getActions().size());

    return resultDomain;
  }

  private List<Permission> getPermissions() {
    Permission permissionView = SecurityHelper.permissionOf(PERMISSION1, DOMAIN_PROJECTS, Constants.Actions.VIEW);
    Permission permissionCreate = SecurityHelper.permissionOf(PERMISSION2, DOMAIN_PROJECTS, Constants.Actions.CREATE);
    Permission permissionUpdate = SecurityHelper.permissionOf(PERMISSION3, DOMAIN_PROJECTS, Constants.Actions.UPDATE);
    Permission permissionDelete = SecurityHelper.permissionOf(PERMISSION4, DOMAIN_PROJECTS, Constants.Actions.DELETE);
    return List.of(permissionView, permissionCreate, permissionUpdate, permissionDelete);
  }

  private List<Permission> prepPermissions() {
    List<Permission> permissions = getPermissions();

    List<Permission> permissionsResult = given()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(permissions)
      .when()
      .post("/api" + INTERNAL_SEGMENT + PERMISSION + "?c_level=3")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", Permission.class);
    assertEquals(permissions.size(), permissionsResult.size());

    return permissions;
  }

  /**
   * Creates role nodes for organization and organization unit targets
   * Adds template permissions both targets.
   * Tests if all data is properly loaded.
   **/
  @Test
  @Order(10)
  public void testLoadTemplatePermissions() {

    this.prepDomain();
    List<Permission> permissions = this.prepPermissions();

    //template node for organizations
    RoleNode roleNodeOrgTemplate = SecurityHelper.roleNodeOf(
        ORG_TEMPLATE_ROLE_NODE_ID, ORG_ENTITY, null, null,null,null
    );
    //organization template permissions
    List<RoleNodePermissionTemplate> roleNodeOrgPermissions = SecurityHelper.prepRoleNodePermissions(
        RoleNodePermissionTemplate.class, roleNodeOrgTemplate, permissions, ORG_ENTITY, null
    );

    //template node for organization units
    RoleNode roleNodeOrgUnitTemplate = SecurityHelper.roleNodeOf(
        ORG_UNIT_TEMPLATE_ROLE_NODE_ID, ORG_UNIT_ENTITY, null, null, null, null
    );
    //organization unit template permissions
    List<RoleNodePermissionTemplate> roleNodeOrgUnitPermissions = SecurityHelper.prepRoleNodePermissions(
        RoleNodePermissionTemplate.class, roleNodeOrgUnitTemplate, permissions, ORG_UNIT_ENTITY, null
    );

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
    RoleNode respRoleOrg = resultOrg.getFirst();
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
    RoleNode respOrgUnit = resultOrgUnit.getFirst();
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

    //load template permissions and check if permissions are set properly

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
    for (RoleNodePermission p : loadedOrgTemplatePermissions) {
      assertInstanceOf(RoleNodePermissionTemplate.class, p);
      assertNotNull(((RoleNodePermissionTemplate)p).getTarget());
    }

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
    for (RoleNodePermission p : loadedOrgUnitTemplatePermissions) {
      assertInstanceOf(RoleNodePermissionTemplate.class, p);
      assertNotNull(((RoleNodePermissionTemplate)p).getTarget());
    }
  }

  /**
   * Creates role nodes for organization and organization unit instances.
   * Creates template permission (DELETE with allowed = false) for organization unit on organization instance role node
   * Loads permissions for organization unit and checks if resolved organization unit DELETE permission is not allowed.
   * Test data prepare by previous test.
   */
  @Test
  @Order(20)
  public void testLoadFirstLevelPermissionTemplates() {
    //role node for organization instance
    RoleNode organizationRoleNode = SecurityHelper.roleNodeOf(
        ORG_ROLE_NODE_ID, ORG_ENTITY, null, ORG_ENTITY, ORG_ENTITY_ID, 0
    );
    //role node for organization unit instance
    RoleNode organizationUnitRoleNode = SecurityHelper.roleNodeOf(
        ORG_UNIT_ROLE_NODE_ID, ORG_UNIT_ENTITY, null, ORG_UNIT_ENTITY, ORG_UNIT_ENTITY_ID, 0
    );
    organizationUnitRoleNode.setParent(organizationRoleNode); //connect to parent node !!!
    //store both nodes
    List<RoleNode> resultOrg = given()
        .log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(List.of(organizationRoleNode))
        .when()
        .post("/api" + INTERNAL_SEGMENT + ROLE_NODE)
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList("data", RoleNode.class);
    assertEquals(1, resultOrg.size());
    resultOrg = given()
        .log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(List.of(organizationUnitRoleNode))
        .when()
        .post("/api" + INTERNAL_SEGMENT + ROLE_NODE + "?c_level=4")
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList("data", RoleNode.class);
    assertEquals(1, resultOrg.size());
    assertEquals(ORG_ROLE_NODE_ID, resultOrg.getFirst().getParent().getUuid().toString());


    //add template permissions for org unit to organization
    List<Permission> permissions = this.getPermissions();
    List<RoleNodePermissionTemplate> roleNodeOrgUnitPermissions = SecurityHelper.prepRoleNodePermissions(
        RoleNodePermissionTemplate.class, organizationRoleNode, permissions, ORG_UNIT_ENTITY, null
    );

    //take one permission (DELETE) and change allowed state to override org unit defaults
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    RoleNodePermission deletePermission = roleNodeOrgUnitPermissions.stream()
        .filter(
            p -> p.getPermission().getAction().getCode().equals(Constants.Actions.DELETE)
        ).peek(
            p -> p.setAllowed(false)
        ).findFirst().get();

    List<RoleNodePermission> resultRoleNodeOrgUnitPermissions = given()
        .log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(List.of(deletePermission))
        .when()
        .post("/api" + INTERNAL_SEGMENT + ROLE_NODE_PERMISSION)
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList("data", RoleNodePermission.class);
    assertEquals(1, resultRoleNodeOrgUnitPermissions.size());
    for (RoleNodePermission p : resultRoleNodeOrgUnitPermissions) {
      assertInstanceOf(RoleNodePermissionTemplate.class, p);
      assertNotNull(((RoleNodePermissionTemplate)p).getTarget());
    }

    //load permissions for org unit
    RoleNodeParams params = new RoleNodeParams();
    params.setEntity(ORG_UNIT_ENTITY);
    params.setEntityId(ORG_UNIT_ENTITY_ID);
    params.getFilter().getContent().setTreeLevel(4);

    List<RoleNodePermission> orgUnitCombinedPermissions = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(params)
      .when()
      .post("/api" + INTERNAL_SEGMENT + PERMISSIONS + PERMISSIONS_LIST_SEGMENT )
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNodePermission.class);

    assertEquals(4, orgUnitCombinedPermissions.size());

    //check if DELETE permission is not allowed as set by ORG template for ORG UNIT
    //noinspection OptionalGetWithoutIsPresent
    assertFalse(
        orgUnitCombinedPermissions.stream().filter(
            p -> p.getPermission().getAction().getCode().equals(Constants.Actions.DELETE)
        ).findFirst().get().getAllowed()
    );
  }




  @Test
  @Order(30)
  public void testLoadAndCheckPermissionInstanceForOrgEntity() {

    //load permissions for org
    RoleNodeParams params = new RoleNodeParams();
    params.setEntity(ORG_ENTITY);
    params.setEntityId(ORG_ENTITY_ID);
    params.getFilter().getContent().setTreeLevel(4);

    List<RoleNodePermission> orgPermissions = given()
        .log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(params)
        .when()
        .post("/api" + INTERNAL_SEGMENT + PERMISSIONS + PERMISSIONS_LIST_SEGMENT )
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList("data", RoleNodePermission.class);

    assertEquals(4, orgPermissions.size());

  }


  @Test
  @Order(35)
  public void testLoadAndCheckPermissionInstanceForOrgUnitEntity() {
    RoleNode organizationUnitRoleNode = SecurityHelper.roleNodeOf(
        ORG_UNIT_ROLE_NODE_ID, ORG_UNIT_ENTITY, null, ORG_UNIT_ENTITY, ORG_UNIT_ENTITY_ID, 0
    );

    List<Permission> permissions = this.getPermissions();
    List<RoleNodePermission> roleNodeOrgUnitPermissions = SecurityHelper.prepRoleNodePermissions(
        RoleNodePermission.class, organizationUnitRoleNode, permissions, null, null
    );

    List<RoleNodePermission> roleNodeOrgUnitInstancePermissions = roleNodeOrgUnitPermissions.stream()
      .filter(p-> !p.getPermission().getAction().getCode().equals(Constants.Actions.DELETE))
      .filter(p-> !p.getPermission().getAction().getCode().equals(Constants.Actions.VIEW))
      .peek(p -> p.setAllowed(false))
      .toList();

    List<RoleNodePermission> resultRoleNodeOrgUnitPermissions = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(roleNodeOrgUnitInstancePermissions)
      .when()
      .post("/api" + INTERNAL_SEGMENT + ROLE_NODE_PERMISSION)
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNodePermission.class);
    assertEquals(2, resultRoleNodeOrgUnitPermissions.size());
    for (RoleNodePermission p : resultRoleNodeOrgUnitPermissions) {
      assertInstanceOf(RoleNodePermission.class, p);
    }

    //load permissions for org unit
    RoleNodeParams params = new RoleNodeParams();
    params.setEntity(ORG_UNIT_ENTITY);
    params.setEntityId(ORG_UNIT_ENTITY_ID);
    params.getFilter().getContent().setTreeLevel(4);

    List<RoleNodePermission> orgUnitCombinedPermissions = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(params)
      .when()
      .post("/api" + INTERNAL_SEGMENT + PERMISSIONS + PERMISSIONS_LIST_SEGMENT )
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList("data", RoleNodePermission.class);

    assertEquals(4, orgUnitCombinedPermissions.size());

    //check permissions states
    //noinspection OptionalGetWithoutIsPresent
    assertTrue(
        orgUnitCombinedPermissions.stream().filter(
            p -> p.getPermission().getAction().getCode().equals(Constants.Actions.VIEW)
        ).findFirst().get().getAllowed()
    );
    //noinspection OptionalGetWithoutIsPresent
    assertFalse(
        orgUnitCombinedPermissions.stream().filter(
            p -> p.getPermission().getAction().getCode().equals(Constants.Actions.DELETE)
        ).findFirst().get().getAllowed()
    );
    //noinspection OptionalGetWithoutIsPresent
    assertFalse(
        orgUnitCombinedPermissions.stream().filter(
            p -> p.getPermission().getAction().getCode().equals(Constants.Actions.CREATE)
        ).findFirst().get().getAllowed()
    );
    //noinspection OptionalGetWithoutIsPresent
    assertFalse(
        orgUnitCombinedPermissions.stream().filter(
            p -> p.getPermission().getAction().getCode().equals(Constants.Actions.UPDATE)
        ).findFirst().get().getAllowed()
    );
  }


  @Test
  @Order(40)
  public void testLoadAndCheckPermissionOFSecondLevelInstanceForEntity() {
    RoleNode organizationUnitRoleNode = SecurityHelper.roleNodeOf(
        ORG_UNIT_ROLE_NODE_ID, ORG_UNIT_ENTITY, null, ORG_UNIT_ENTITY, ORG_UNIT_ENTITY_ID, 0
    );

    //Create role node for user on organization unit
    RoleNode organizationUnitUserRoleNode = SecurityHelper.roleNodeOf(
        USER_ROLE_NODE_ID, ORG_UNIT_ENTITY, null, USER_ENTITY, USER_ENTITY_ID, 1
    );
    organizationUnitUserRoleNode.setParent(organizationUnitRoleNode); //connect to parent node !!!

    List<RoleNode> resultOrg = given()
        .log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(List.of(organizationUnitUserRoleNode))
        .when()
        .post("/api" + INTERNAL_SEGMENT + ROLE_NODE + "?c_level=4")
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList("data", RoleNode.class);
    assertEquals(1, resultOrg.size());

    List<Permission> permissions = this.getPermissions();
    List<RoleNodePermission> roleNodeOrgUnitUserPermissions = SecurityHelper.prepRoleNodePermissions(
        RoleNodePermission.class, organizationUnitRoleNode, permissions, null, null
    );

    List<RoleNodePermission> roleNodeOrgUnitInstancePermissions = roleNodeOrgUnitUserPermissions.stream()
        .filter(
            p-> p.getPermission().getAction().getCode().equals(Constants.Actions.VIEW)
        ).peek(
            p -> p.setAllowed(false)
        ).toList();

    //Store role node permissions for user of organization unit
    List<RoleNodePermission> resultRoleNodeOrgUnitPermissions = given()
        .log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(roleNodeOrgUnitInstancePermissions)
        .when()
        .post("/api" + INTERNAL_SEGMENT + ROLE_NODE_PERMISSION + "?c_level=4")
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList("data", RoleNodePermission.class);
    assertEquals(1, resultRoleNodeOrgUnitPermissions.size());
    for (RoleNodePermission p : resultRoleNodeOrgUnitPermissions) {
      assertInstanceOf(RoleNodePermission.class, p);
    }

    //load permissions for org unit user
    RoleNodeParams params = new RoleNodeParams();
    params.setEntity(ORG_UNIT_ENTITY);
    params.setEntityId(ORG_UNIT_ENTITY_ID);
    params.getFilter().getContent().setTreeLevel(4);

    List<RoleNodePermission> orgUnitUserCombinedPermissions = given()
        .log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(params)
        .when()
        .post("/api" + INTERNAL_SEGMENT + PERMISSIONS + PERMISSIONS_LIST_SEGMENT  + "?c_level=4")
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList("data", RoleNodePermission.class);

    assertEquals(4, orgUnitUserCombinedPermissions.size());

    //check permissions states
    //noinspection OptionalGetWithoutIsPresent
    assertFalse(
        orgUnitUserCombinedPermissions.stream().filter(
            p -> p.getPermission().getAction().getCode().equals(Constants.Actions.VIEW)
        ).findFirst().get().getAllowed()
    );
    //noinspection OptionalGetWithoutIsPresent
    assertFalse(
        orgUnitUserCombinedPermissions.stream().filter(
            p -> p.getPermission().getAction().getCode().equals(Constants.Actions.DELETE)
        ).findFirst().get().getAllowed()
    );
    //noinspection OptionalGetWithoutIsPresent
    assertFalse(
        orgUnitUserCombinedPermissions.stream().filter(
            p -> p.getPermission().getAction().getCode().equals(Constants.Actions.CREATE)
        ).findFirst().get().getAllowed()
    );
    //noinspection OptionalGetWithoutIsPresent
    assertFalse(
        orgUnitUserCombinedPermissions.stream().filter(
            p -> p.getPermission().getAction().getCode().equals(Constants.Actions.UPDATE)
        ).findFirst().get().getAllowed()
    );
  }

}
