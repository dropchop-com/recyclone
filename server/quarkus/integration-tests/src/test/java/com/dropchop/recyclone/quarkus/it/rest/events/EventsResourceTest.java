package com.dropchop.recyclone.quarkus.it.rest.events;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.Field;
import com.dropchop.recyclone.base.dto.model.event.Event;
import com.dropchop.recyclone.base.dto.model.event.EventDetail;
import com.dropchop.recyclone.base.dto.model.event.EventItem;
import com.dropchop.recyclone.base.dto.model.event.EventTrace;
import com.dropchop.recyclone.base.dto.model.invoke.EventParams;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.query.Condition.*;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 9. 12. 24.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventsResourceTest {

  @Inject
  ObjectMapper mapper;

  @BeforeEach
  public void setUp() {
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
        objectMapperConfig().jackson2ObjectMapperFactory((type, s) -> mapper)
    );
  }

  public static String EVENT_ID = UUID.randomUUID().toString();
  public static String EVENT_DETAIL_ID = UUID.randomUUID().toString();
  public static String EVENT_TRACE_ID = UUID.randomUUID().toString();

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

  private void validate(List<Event> events) {
    //assertEquals(1, events.size());
    Event rspEvent = events.getFirst();
    assertEquals(EVENT_ID, rspEvent.getId());
    assertNotNull(rspEvent.getCreated());
    assertEquals(Strings.APPLICATION, rspEvent.getApplication());
    assertEquals(Strings.TYPE, rspEvent.getType());
    assertEquals(Strings.ACTION, rspEvent.getAction());
    assertEquals(Strings.DATA, rspEvent.getData());
    //assertEquals(rspEvent.getValue(), rspEvent.getValue());
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

      /*assertNotNull(rspEvent.getAttributeValue(Strings.ATTRIBUTE_STR));
    assertEquals(Strings.ATTRIBUTE_STR, rspEvent.getAttributeValue(Strings.ATTRIBUTE_STR));
    assertEquals(1, ((BigDecimal)rspEvent.getAttributeValue(Strings.ATTRIBUTE_NUM)).intValue());
    assertEquals(true, rspEvent.getAttributeValue(Strings.ATTRIBUTE_BOOL));*/

  }

  private EventDetail createMockEventDetailSubject(String name, EventDetail parent, EventDetail child) {
    return createMockEventDetail(name, parent, child);
  }

  private EventDetail createMockEventDetailObject(String name, EventDetail parent, EventDetail child)
  {
    return createMockEventDetail(name, parent, child);
  }

  private EventDetail createMockEventDetailService(String name, EventDetail parent, EventDetail child)
  {
    return createMockEventDetail(name, parent, child);
  }

  private EventDetail createMockEventDetailContext(String name, EventDetail parent, EventDetail child)
  {
    return createMockEventDetail(name, parent, child);
  }

  private EventDetail createMockEventDetail(String name, EventDetail parent, EventDetail child) {
    EventDetail detail = new EventDetail();

    detail.setId(UUID.randomUUID().toString());
    detail.setCreated(ZonedDateTime.now());
    detail.setParent(parent);
    detail.setChild(child);
    detail.setCreated(ZonedDateTime.now());

    return detail;

  }

  private EventItem createMockEventItem(String type, EventDetail subject, EventDetail object, EventDetail service, EventDetail context)
  {
    EventItem item = new EventItem();
    item.setId(UUID.randomUUID().toString());
    item.setSubject(subject);
    item.setObject(object);
    item.setService(service);
    item.setContext(context);
    item.setCreated(ZonedDateTime.now());

    return item;
  }

  private EventTrace createMockEventTrace(String id,String group, String context)
  {
    EventTrace trace = new EventTrace();
    trace.setId(id);
    trace.setGroup(group);
    trace.setContext(context);
    return trace;
  }

  public Event createMockEvent(String application, String type, String action)
  {
    Event event = new Event();
    event.setId(UUID.randomUUID().toString());
    event.setApplication(application);
    event.setType(type);
    event.setAction(action);

    EventItem source = createMockEventItem(
            "source",
            createMockEventDetailSubject("Source_Subject", null, null),
            createMockEventDetailObject("Source_Object", null, null),
            createMockEventDetailService("Source_Service", null, null),
            createMockEventDetailContext("Source_Context", null, null)
    );
    EventItem target = createMockEventItem(
            "Target",
            createMockEventDetailSubject("Target_Subject", null, null),
            createMockEventDetailObject("Target_Object", null, null),
            createMockEventDetailService("Target_Service", null, null),
            createMockEventDetailContext("Target_Context", null, null)
    );
    EventItem cause = createMockEventItem(
            "Cause",
            createMockEventDetailSubject("Cause_Subject", null, null),
            createMockEventDetailObject("Cause_Object", null, null),
            createMockEventDetailService("Cause_Service", null, null),
            createMockEventDetailContext("Cause_Context", null, null)
    );

    // Set trace
    EventTrace trace = createMockEventTrace(EVENT_TRACE_ID ,"Mock_Group", "Mock_Context");
    //EventTrace trace2 = createMockEventTrace( , "Mock_Group2", "Mock_Context2");
    //EventTrace trace3 = createMockEventTrace(EVENT_TRACE_ID , "Mock Group3", "Mock_Context3");

    // Populate event
    event.setSource(source);
    event.setTarget(target);
    event.setCause(cause);
    event.setTrace(trace);
    event.setCreated(ZonedDateTime.now());
    event.setValue(1.0);
    event.setUnit("Mock_Unit");
    return event;
  }

  public List<Event> createMockEvents()
  {
    List<Event> mockEvents = new ArrayList<>();
    mockEvents.add(createMockEvent("User app", "USER_ACTION", "BUTTON_CLICK"));
    mockEvents.add(createMockEvent("BackendService", "API_CALL", "FETCH_DATA"));
    mockEvents.add(createMockEvent("EmailService", "EMAIL", "SEND"));
    mockEvents.add(createMockEvent("SearchModule", "ES_QUERY", "SEARCH"));
    mockEvents.add(createMockEvent("AuthModule", "USER_REGISTRATION", "CREATE_ACCOUNT"));

    return mockEvents;
  }


  @Test
  @Order(10)
  public void create() {

    EventDetail detail = new EventDetail();
    detail.setCreated(ZonedDateTime.now());
    detail.setId(EVENT_DETAIL_ID);
    detail.setName(Strings.EVENT_DETAIL);

    EventItem eventItem = new EventItem();
    eventItem.setCreated(ZonedDateTime.now());
    eventItem.setSubject(detail);
    eventItem.setObject(detail);
    eventItem.setService(detail);
    eventItem.setContext(detail);

    EventTrace eventTrace = new EventTrace();
    eventTrace.setId(EVENT_TRACE_ID);
    eventTrace.setContext(Strings.CONTEXT);
    eventTrace.setGroup(Strings.GROUP);

    Event event = new Event();
    event.setId(EVENT_ID);
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

    //TODO: implement attributes
    /*event.addAttribute(new AttributeString(Strings.ATTRIBUTE_STR, Strings.ATTRIBUTE_STR));
    event.addAttribute(new AttributeDecimal(Strings.ATTRIBUTE_NUM, 1));
    event.addAttribute(new AttributeBool(Strings.ATTRIBUTE_BOOL, true));*/

    List<Event> events = given()
        .log().all()
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
    this.validate(events);
  }

  @Test
  @Order(20)
  public void search() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ignored) {}

    Condition c = or(
      field("uuid", EVENT_ID)
    );

    EventParams params = EventParams.builder().condition(c).build();
    params.tryGetResultFilter().setSize(100);
    params.tryGetResultFilter().getContent().setTreeLevel(5);

    List<Event> events = given()
        .log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        .auth().preemptive().basic("admin1", "password")
        .and()
        .body(params)
        .when()
        .post("/api/internal/events/search")
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList(".", Event.class);

    this.validate(events);
  }

  @Test
  @Order(25)
  public void get() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ignored) {}

    List<Event> events = given()
        .log().all()
        .contentType(ContentType.JSON)
        .accept(MediaType.APPLICATION_JSON)
        .auth().preemptive().basic("admin1", "password")
        .and()
        .when()
        .get("/api/internal/events/" + EVENT_ID + "?c_level=5" )
        .then()
        .statusCode(200)
        .extract()
        .body().jsonPath().getList(".", Event.class);

    this.validate(events);

  }

  @Test
  @Order(30)
  public void delete() {

    Event event = new Event();
    event.setId(EVENT_ID);

    List<Event> events = given()
        .log().all()
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

    try {
      Thread.sleep(5000);
    } catch (InterruptedException ignored) {}

    events = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(List.of(event))
      .when()
      .post("/api/internal/events/search?c_level=5")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Event.class);

    assertEquals(0, events.size());
  }

  @Test
  @Order(35)
  public void createMultiple() {
    List<Event> events = createMockEvents();

    //same as before just for multiple events
    given()
            .log().all()
            .contentType(ContentType.JSON)
            .accept(MediaType.APPLICATION_JSON)
            .auth().preemptive().basic("admin1", "password")
            .and()
            .body(events)
            .when()
            .post("/api/internal/events/?c_level=5")
            .then()
            .statusCode(200)
            .extract()
            .body().jsonPath().getList(".", Event.class);

    assertEquals(5, events.size());

  }

  @Test
  @Order(40)
  public void searchBySameTraceId()
  {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ignored) {}



    Field field = field("uuid", EVENT_TRACE_ID);
    Condition c = and(field);

    EventParams params = EventParams.builder().condition(
            and(c)
    ).build();
    params.tryGetResultFilter().setSize(100);
    params.tryGetResultFilter().getContent().setTreeLevel(5);

    List<Event> events = given()
            .log().all()
            .contentType(ContentType.JSON)
            .accept(MediaType.APPLICATION_JSON)
            .auth().preemptive().basic("admin1", "password")
            .and()
            .body(params)
            .when()
            .post("/api/internal/events/search")
            .then()
            .statusCode(200)
            .extract()
            .body().jsonPath().getList(".", Event.class);

            this.validate(events);
  }

}
