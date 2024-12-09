package com.dropchop.recyclone.quarkus.it.rest.events;

import com.dropchop.recyclone.model.api.rest.Constants;
import com.dropchop.recyclone.model.dto.event.Event;
import com.dropchop.recyclone.model.dto.event.EventDetail;
import com.dropchop.recyclone.model.dto.event.EventItem;
import com.dropchop.recyclone.model.dto.event.EventTrace;
import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.rest.api.MediaType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 9. 12. 24.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventsResourceTest {

  public static UUID EVENT_ID = UUID.randomUUID();
  public static UUID EVENT_DETAIL_ID = UUID.randomUUID();
  public static UUID EVENT_TRACE_ID = UUID.randomUUID();

  interface Strings {
    String EVENT_DETAIL = "event_detail";
    String CONTEXT = "context";
    String GROUP = "group";
    String APPLICATION = "application";
    String TYPE = "type";
    String ACTION = "action";
    String DATA = "data";
    String UNIT = "unit";
  }


  @Test
  @Order(10)
  public void create() {

    EventDetail detail = new EventDetail();
    detail.setCreated(ZonedDateTime.now());
    detail.setId(EVENT_DETAIL_ID.toString());
    detail.setName(Strings.EVENT_DETAIL);

    EventItem eventItem = new EventItem();
    eventItem.setCreated(ZonedDateTime.now());
    eventItem.setSubject(detail);
    eventItem.setObject(detail);
    eventItem.setService(detail);
    eventItem.setContext(detail);

    EventTrace eventTrace = new EventTrace();
    eventTrace.setId(EVENT_TRACE_ID.toString());
    eventTrace.setContext(Strings.CONTEXT);
    eventTrace.setGroup(Strings.GROUP);

    Event event = new Event();
    event.setId(EVENT_ID.toString());
    event.setCreated(ZonedDateTime.now());
    event.setApplication(Strings.APPLICATION);
    event.setType(Strings.TYPE);
    event.setAction(Strings.ACTION);
    event.setData(Strings.DATA);
    event.setValue(1D);
    event.setUnit(Strings.UNIT);
    event.setSource(eventItem);
    event.setCause(eventItem);
    event.setTarget(eventItem);
    event.setTrace(eventTrace);

    List<Event> events = given()
        //.log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        //.header("Authorization", "Bearer editortoken1")
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(List.of(event))
        .when()
        .post("/api/internal/events/?c_level=5")
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList(".", Event.class);
    assertEquals(1, events.size());
    Event rspEvent = events.get(0);
    assertEquals(event.getId(), rspEvent.getId());
    assertNotNull(event.getCreated());
    assertEquals(Strings.APPLICATION, rspEvent.getApplication());
    assertEquals(Strings.TYPE, rspEvent.getType());
    assertEquals(Strings.ACTION, rspEvent.getAction());
    assertEquals(Strings.DATA, rspEvent.getData());
    assertEquals(event.getValue(), rspEvent.getValue());
    assertEquals(Strings.UNIT, rspEvent.getUnit());
    assertNotNull(event.getSource());
    assertNotNull(event.getSource().getSubject());
    assertNotNull(event.getSource().getSubject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, detail.getName());
    assertNotNull(event.getSource().getObject());
    assertNotNull(event.getSource().getObject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, detail.getName());
    assertNotNull(event.getSource().getService());
    assertNotNull(event.getSource().getService().getCreated());
    assertEquals(Strings.EVENT_DETAIL, detail.getName());
    assertNotNull(event.getSource().getContext());
    assertNotNull(event.getSource().getContext().getCreated());
    assertEquals(event.getSource().getContext().getName(), detail.getName());
    assertNotNull(event.getCause());
    assertNotNull(event.getCause().getSubject());
    assertNotNull(event.getCause().getSubject().getCreated());
    assertEquals(event.getCause().getSubject().getName(), detail.getName());
    assertNotNull(event.getCause().getObject());
    assertNotNull(event.getCause().getObject().getCreated());
    assertEquals(event.getCause().getObject().getName(), detail.getName());
    assertNotNull(event.getCause().getService());
    assertNotNull(event.getCause().getService().getCreated());
    assertEquals(event.getCause().getService().getName(), detail.getName());
    assertNotNull(event.getCause().getContext());
    assertNotNull(event.getCause().getContext().getCreated());
    assertEquals(event.getCause().getContext().getName(), detail.getName());
    assertNotNull(event.getTarget());
    assertNotNull(event.getTarget().getSubject());
    assertNotNull(event.getTarget().getSubject().getCreated());
    assertEquals(event.getTarget().getSubject().getName(), detail.getName());
    assertNotNull(event.getTarget().getObject());
    assertNotNull(event.getTarget().getObject().getCreated());
    assertEquals(event.getTarget().getObject().getName(), detail.getName());
    assertNotNull(event.getTarget().getService());
    assertNotNull(event.getTarget().getService().getCreated());
    assertEquals(event.getTarget().getService().getName(), detail.getName());
    assertNotNull(event.getTarget().getContext());
    assertNotNull(event.getTarget().getContext().getCreated());
    assertEquals(event.getTarget().getContext().getName(), detail.getName());
    assertNotNull(event.getTrace());
    assertEquals(event.getTrace().getContext(), eventTrace.getContext());
    assertEquals(event.getTrace().getGroup(), eventTrace.getGroup());
  }


  @Test
  @Order(20)
  public void get() {
    Event event = new Event();
    event.setId(EVENT_ID.toString());

    List<Event> events = given()
        //.log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        //.header("Authorization", "Bearer editortoken1")
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(List.of(event))
        .when()
        .delete("/api/internal/events/?c_level=5")
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList(".", Event.class);



  }



  @Test
  @Order(30)
  public void delete() {

    Event event = new Event();
    event.setId(EVENT_ID.toString());

    List<Event> events = given()
        //.log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        //.header("Authorization", "Bearer editortoken1")
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(List.of(event))
        .when()
        .delete("/api/internal/events/?c_level=5")
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList(".", Event.class);
  }


}
