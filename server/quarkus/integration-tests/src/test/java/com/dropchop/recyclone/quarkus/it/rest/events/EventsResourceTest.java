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
    assertNotNull(rspEvent.getSource());
    assertNotNull(rspEvent.getSource().getSubject());
    assertNotNull(rspEvent.getSource().getSubject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getSource().getSubject().getName());
    assertNotNull(rspEvent.getSource().getObject());
    assertNotNull(rspEvent.getSource().getObject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getSource().getObject().getName());
    assertNotNull(rspEvent.getSource().getService());
    assertNotNull(rspEvent.getSource().getService().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getSource().getService().getName());
    assertNotNull(rspEvent.getSource().getContext());
    assertNotNull(rspEvent.getSource().getContext().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getSource().getContext().getName());
    assertNotNull(rspEvent.getCause());
    assertNotNull(rspEvent.getCause().getSubject());
    assertNotNull(rspEvent.getCause().getSubject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getCause().getSubject().getName());
    assertNotNull(rspEvent.getCause().getObject());
    assertNotNull(rspEvent.getCause().getObject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getCause().getObject().getName());
    assertNotNull(rspEvent.getCause().getService());
    assertNotNull(rspEvent.getCause().getService().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getCause().getService().getName());
    assertNotNull(rspEvent.getCause().getContext());
    assertNotNull(rspEvent.getCause().getContext().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getCause().getContext().getName());
    assertNotNull(rspEvent.getTarget());
    assertNotNull(rspEvent.getTarget().getSubject());
    assertNotNull(rspEvent.getTarget().getSubject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getTarget().getSubject().getName());
    assertNotNull(rspEvent.getTarget().getObject());
    assertNotNull(rspEvent.getTarget().getObject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getTarget().getObject().getName());
    assertNotNull(rspEvent.getTarget().getService());
    assertNotNull(rspEvent.getTarget().getService().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getTarget().getService().getName());
    assertNotNull(rspEvent.getTarget().getContext());
    assertNotNull(rspEvent.getTarget().getContext().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getTarget().getContext().getName());
    assertNotNull(rspEvent.getTrace());
    assertEquals(Strings.CONTEXT, rspEvent.getTrace().getContext());
    assertEquals(Strings.GROUP, rspEvent.getTrace().getGroup());
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
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(List.of(event))
        .when()
        .delete("/api/internal/events/?c_level=5")
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
    assertNotNull(rspEvent.getSource());
    assertNotNull(rspEvent.getSource().getSubject());
    assertNotNull(rspEvent.getSource().getSubject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getSource().getSubject().getName());
    assertNotNull(rspEvent.getSource().getObject());
    assertNotNull(rspEvent.getSource().getObject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getSource().getObject().getName());
    assertNotNull(rspEvent.getSource().getService());
    assertNotNull(rspEvent.getSource().getService().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getSource().getService().getName());
    assertNotNull(rspEvent.getSource().getContext());
    assertNotNull(rspEvent.getSource().getContext().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getSource().getContext().getName());
    assertNotNull(rspEvent.getCause());
    assertNotNull(rspEvent.getCause().getSubject());
    assertNotNull(rspEvent.getCause().getSubject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getCause().getSubject().getName());
    assertNotNull(rspEvent.getCause().getObject());
    assertNotNull(rspEvent.getCause().getObject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getCause().getObject().getName());
    assertNotNull(rspEvent.getCause().getService());
    assertNotNull(rspEvent.getCause().getService().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getCause().getService().getName());
    assertNotNull(rspEvent.getCause().getContext());
    assertNotNull(rspEvent.getCause().getContext().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getCause().getContext().getName());
    assertNotNull(rspEvent.getTarget());
    assertNotNull(rspEvent.getTarget().getSubject());
    assertNotNull(rspEvent.getTarget().getSubject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getTarget().getSubject().getName());
    assertNotNull(rspEvent.getTarget().getObject());
    assertNotNull(rspEvent.getTarget().getObject().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getTarget().getObject().getName());
    assertNotNull(rspEvent.getTarget().getService());
    assertNotNull(rspEvent.getTarget().getService().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getTarget().getService().getName());
    assertNotNull(rspEvent.getTarget().getContext());
    assertNotNull(rspEvent.getTarget().getContext().getCreated());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getTarget().getContext().getName());
    assertNotNull(rspEvent.getTrace());
    assertEquals(Strings.CONTEXT, rspEvent.getTrace().getContext());
    assertEquals(Strings.GROUP, rspEvent.getTrace().getGroup());

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
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(List.of(event))
        .when()
        .delete("/api/internal/events/?c_level=5")
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList(".", Event.class);

    assertEquals(1, events.size());

    events = given()
      //.log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(event))
      .when()
      .delete("/api/internal/events/?c_level=5")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Event.class);

    assertEquals(0, events.size());
  }


}
