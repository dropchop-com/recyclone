package com.dropchop.recyclone.test.quarkus;

import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 25. 05. 22.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PermissionResourceTest {

  //@Test
  //@Order(10)
  public void languagesPostEndpoint() {
    Domain domain = new Domain();
    domain.setCode(Constants.Domains.Localization.LANGUAGE);
    Action action = new Action();
    action.setCode(Constants.Actions.ALL);
    Permission permission = new Permission();
    permission.setUuid("");
    permission.setAction(action);
    permission.setDomain(domain);
    permission.setTitle("Permit all actions on Language");
    permission.setLang("sl");
    permission.addTranslation(new TitleTranslation("sl", "Dovoli vse akcije na jezikih."));

    List<Permission> permissions = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      //.header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("editor1", "password")
      .and()
      .body(List.of(permission))
      .when()
      .post("/api/internal/security/permission")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Permission.class);
    assertEquals(1, permissions.size());
    assertEquals(permission, permissions.get(0));
  }
}
