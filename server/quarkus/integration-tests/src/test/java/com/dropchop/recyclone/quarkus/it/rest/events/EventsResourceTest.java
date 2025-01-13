package com.dropchop.recyclone.quarkus.it.rest.events;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.ConditionOperator;
import com.dropchop.recyclone.base.api.model.query.Field;
import com.dropchop.recyclone.base.api.model.query.Query;
import com.dropchop.recyclone.base.api.model.query.condition.Not;
import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.dropchop.recyclone.base.dto.model.event.Event;
import com.dropchop.recyclone.base.dto.model.event.EventDetail;
import com.dropchop.recyclone.base.dto.model.event.EventItem;
import com.dropchop.recyclone.base.dto.model.event.EventTrace;
import com.dropchop.recyclone.base.dto.model.invoke.EventParams;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.query.Condition.*;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.*;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.junit.jupiter.api.Assertions.*;

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

  public static String EVENT_ID =  "feea39e2-aea0-4395-8be3-dd42ca42f03c";
  public static String EVENT_DETAIL_ID = "eebd0fda-9e81-4fa8-a4c6-d3cdbc06e4c8";
  public static String EVENT_TRACE_ID = "6df37bd3-073f-45f2-9f00-65022f2b4019";

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

  private EventDetail createMockEventDetailSubject(String id,String name, EventDetail parent, EventDetail child, ZonedDateTime created) {
    return createMockEventDetail(id,name, parent, child, created);
  }

  private EventDetail createMockEventDetailObject(String id,String name, EventDetail parent, EventDetail child, ZonedDateTime created)
  {
    return createMockEventDetail(id,name, parent, child, created);
  }

  private EventDetail createMockEventDetailService(String id,String name, EventDetail parent, EventDetail child, ZonedDateTime created)
  {
    return createMockEventDetail(id,name, parent, child,created);
  }

  private EventDetail createMockEventDetailContext(String id,String name, EventDetail parent, EventDetail child, ZonedDateTime created)
  {
    return createMockEventDetail(id,name, parent, child, created);
  }

  private EventDetail createMockEventDetail(String id,String name, EventDetail parent, EventDetail child, ZonedDateTime created) {
    EventDetail detail = new EventDetail();

    detail.setId(id);
    detail.setCreated(created);
    detail.setParent(parent);
    detail.setChild(child);

    return detail;

  }

  private EventItem createMockEventItem(String type, EventDetail subject, EventDetail object, EventDetail service, EventDetail context, ZonedDateTime created)
  {
    EventItem item = new EventItem();
    item.setId(UUID.randomUUID().toString());
    item.setSubject(subject);
    item.setObject(object);
    item.setService(service);
    item.setContext(context);
    item.setCreated(created);

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

  public List<Event> createMockEventsForTraceTest()
  {
      List<Event> mockEvents = new ArrayList<>();
      Event event = new Event();
      event.setId("4794b019-9750-44f4-a3c9-33516c6bfc50");
      event.setApplication("Baucheck-backend");
      event.setType("Backend");
      event.setAction("FETCH_DATA");

    ZonedDateTime created = ZonedDateTime.of(2024,1,1,0,1,15, 20,ZoneId.of("UTC"));

    EventItem source = createMockEventItem(
            "source",
            createMockEventDetailSubject("d7e5ad61-0a9d-4493-ad73-a3e9a9d42999","Source_Subject", null, null, created),
            createMockEventDetailObject("96fa1d85-c1c1-4f06-b7d7-858ef70002b3","Source_Object", null, null, created),
            createMockEventDetailService("c3b12126-7bf5-4ce1-955c-db73df6e5bd6","Source_Service", null, null, created),
            createMockEventDetailContext("f8dea019-a47e-4652-b3f1-69f7ebd610b3","Source_Context", null, null, created),
            created
    );
    EventItem target = createMockEventItem(
            "Target",
            createMockEventDetailSubject("d7e5ad61-0a9d-4493-ad73-a3e9a9d42999","Target_Subject", null, null, created),
            createMockEventDetailObject("96fa1d85-c1c1-4f06-b7d7-858ef70002b3","Target_Object", null, null, created),
            createMockEventDetailService("c3b12126-7bf5-4ce1-955c-db73df6e5bd6","Target_Service", null, null, created),
            createMockEventDetailContext("f8dea019-a47e-4652-b3f1-69f7ebd610b3","Target_Context", null, null, created),
            created
    );
    EventItem cause = createMockEventItem(
            "Cause",
            createMockEventDetailSubject("d7e5ad61-0a9d-4493-ad73-a3e9a9d42999","Cause_Subject", null, null, created),
            createMockEventDetailObject("96fa1d85-c1c1-4f06-b7d7-858ef70002b3","Cause_Object", null, null, created),
            createMockEventDetailService("c3b12126-7bf5-4ce1-955c-db73df6e5bd6","Cause_Service", null, null, created),
            createMockEventDetailContext("f8dea019-a47e-4652-b3f1-69f7ebd610b3","Cause_Context", null, null, created),
            created
    );

    EventTrace trace = createMockEventTrace(EVENT_TRACE_ID,"Mock_Group", "Mock_Context");

    event.setSource(source);
    event.setTarget(target);
    event.setCause(cause);
    event.setTrace(trace);
    event.setCreated(created);
    event.setValue(1.0);
    event.setUnit("Mock_Unit");

    mockEvents.add(event);

    Event event2 = new Event();
    event2.setId("6b829aac-06d2-4cbc-9721-d6d24a3628dd");
    event2.setApplication("Baucheck-backend");
    event2.setType("Backend");
    event2.setAction("DISPLAY_DATA");
    ZonedDateTime created2 = ZonedDateTime.of(2024, 6, 15, 12, 30, 45, 0, ZoneId.of("UTC"));

    EventItem source2 = createMockEventItem(
            "source",
            createMockEventDetailSubject("9579ba79-282e-4d24-b2a6-6b7c23cdc9a1","Source_Subject2", null, null,created2),
            createMockEventDetailObject("e445b717-6cad-4218-aefb-ca2b4ecf6cd4","Source_Object2", null, null,created2),
            createMockEventDetailService("a4714d43-f459-4f32-b0f4-1b09fc345c6e","Source_Service2", null, null,created2),
            createMockEventDetailContext("5e5aad1a-6b53-4311-81cf-82e7a5b5dda1","Source_Context2", null, null,created2),
            created2
    );
    EventItem target2 = createMockEventItem(
            "Target",
            createMockEventDetailSubject("ee76f4b4-33a0-4e86-8605-334ee48e2df9","Target_Subject2", null, null,created2),
            createMockEventDetailObject("e9db69e0-42a0-4b3c-9d2e-00ff47b88288","Target_Object2", null, null,created2),
            createMockEventDetailService("01101d6a-1c81-4b73-a0ec-d58d38bbdc7f","Target_Service2", null, null,created2),
            createMockEventDetailContext("e3a17276-aa58-46c5-be7c-5888dd1ba13e","Target_Context2", null, null,created2),
            created2
    );
    EventItem cause2 = createMockEventItem(
            "Cause",
            createMockEventDetailSubject("ce5e1c87-06fd-49bd-8012-09d4f9544aad","Cause_Subject2", null, null,created2),
            createMockEventDetailObject("858e731e-c98d-45f8-8a11-b794b7fadbf5","Cause_Object2", null, null,created2),
            createMockEventDetailService("0bbd008a-fda5-49f5-9cfc-e87cf37c6c3e","Cause_Service2", null, null,created2),
            createMockEventDetailContext("d0644ca7-80fb-4560-af63-8b1e93e5e1d3","Cause_Context2", null, null,created2),
            created2
    );



    event2.setSource(source2);
    event2.setTarget(target2);
    event2.setCause(cause2);
    event2.setTrace(trace); //use the same trace so we can later check query for events with same trace ids
    event2.setCreated(created2);
    event2.setValue(1.0);
    event2.setUnit("Mock_Unit");

    mockEvents.add(event2);


    Event event3 = new Event();
    event3.setId("5c767215-102b-4279-8c8a-8840b949df99");
    event3.setApplication("Baucheck-backend");
    event3.setType("PRINT_DATA");
    ZonedDateTime created3  = ZonedDateTime.of(2025, 12, 31, 23, 59, 59, 0, ZoneId.of("UTC"));

    EventItem source3 = createMockEventItem(
            "source",
            createMockEventDetailSubject("b92e8daa-b240-46dc-826a-e14b9723fc92","Source_Subject3", null, null,created3),
            createMockEventDetailObject("e4212462-4b1a-498b-9a69-5eb85adae2aa","Source_Object3", null, null,created3),
            createMockEventDetailService("76e4dcef-ebe5-4d54-bbe1-5febddb2991b","Source_Service3", null, null,created3),
            createMockEventDetailContext("82b62754-b9f9-4e5a-82dc-0bfeb58d113b","Source_Context3", null, null,created3),
            created3
    );
    EventItem target3 = createMockEventItem(
            "Target",
            createMockEventDetailSubject("b92e8daa-b240-46dc-826a-e14b9723fc92", "Target_Subject3",null, null,created3),
            createMockEventDetailObject("e4212462-4b1a-498b-9a69-5eb85adae2aa","Target_Object3", null, null,created3),
            createMockEventDetailService("76e4dcef-ebe5-4d54-bbe1-5febddb2991b","Target_Service3", null, null,created3),
            createMockEventDetailContext("82b62754-b9f9-4e5a-82dc-0bfeb58d113b","Target_Context3", null, null,created3),
            created3
    );
    EventItem cause3 = createMockEventItem(
            "Cause",
            createMockEventDetailSubject("b92e8daa-b240-46dc-826a-e14b9723fc92","Cause_Subject3", null, null,created3),
            createMockEventDetailObject("e4212462-4b1a-498b-9a69-5eb85adae2aa","Cause_Object3", null, null,created3),
            createMockEventDetailService("76e4dcef-ebe5-4d54-bbe1-5febddb2991b","Cause_Service3", null, null,created3),
            createMockEventDetailContext("82b62754-b9f9-4e5a-82dc-0bfeb58d113b","Cause_Context3", null, null,created3),
            created3
    );


    event3.setSource(source3);
    event3.setTarget(target3);
    event3.setCause(cause3);
    event3.setTrace(trace); //use the same trace so we can later check query for events with same trace ids
    event3.setCreated(created3);
    event3.setValue(1.0);
    event3.setUnit("Mock_Unit");


    mockEvents.add(event3);


    Event event4 = new Event();
    event4.setId("7172db74-524f-4898-b7c9-d3ff54330fbf");
    event4.setApplication("Lupitpole-frontend");
    event4.setType("frontend");
    event4.setAction("SUBMIT_FORM_FRONTEND");
    ZonedDateTime created4 = ZonedDateTime.of(2025, 9, 20, 6, 55, 59, 0, ZoneId.of("UTC"));

    EventItem source4 = createMockEventItem(
            "source",
            createMockEventDetailSubject("d547869d-0ca4-41c5-a8fd-54ee58934a82","Source_Subject4", null, null,created4),
            createMockEventDetailObject("dc654f9d-1138-485e-9d8f-1caf6eed4591","Source_Object4", null, null,created4),
            createMockEventDetailService("f37f308f-5649-4f85-a9c8-e89c850a2e2a","Source_Service4", null, null,created4),
            createMockEventDetailContext("1e17af47-2875-4766-bb77-73e340ded8b8","Source_Context4", null, null,created4),
            created4
    );
    EventItem target4 = createMockEventItem(
            "Target",
            createMockEventDetailSubject("d547869d-0ca4-41c5-a8fd-54ee58934a82","Target_Subject4", null, null,created4),
            createMockEventDetailObject("dc654f9d-1138-485e-9d8f-1caf6eed4591","Target_Object4", null, null,created4),
            createMockEventDetailService("f37f308f-5649-4f85-a9c8-e89c850a2e2a","Target_Service4", null, null,created4),
            createMockEventDetailContext("1e17af47-2875-4766-bb77-73e340ded8b8","Target_Context4", null, null,created4),
            created4
    );
    EventItem cause4 = createMockEventItem(
            "Cause",
            createMockEventDetailSubject("d547869d-0ca4-41c5-a8fd-54ee58934a82","Cause_Subject4", null, null,created4),
            createMockEventDetailObject("dc654f9d-1138-485e-9d8f-1caf6eed4591","Cause_Object4", null, null,created4),
            createMockEventDetailService("f37f308f-5649-4f85-a9c8-e89c850a2e2a","Cause_Service4", null, null,created4),
            createMockEventDetailContext("1e17af47-2875-4766-bb77-73e340ded8b8","Cause_Context4", null, null,created4),
            created4
    );
    EventTrace trace2 = createMockEventTrace("b4aa882d-399a-4e49-a42b-c47fbab66167", "Some group", "Some context");

    event4.setSource(source4);
    event4.setTarget(target4);
    event4.setCause(cause4);
    event4.setTrace(trace2); //we use different trace because we want to exclude some events for query testing purposes
    event4.setCreated(created4);
    event4.setValue(1.0);
    event4.setUnit("Mock_Unit");

    mockEvents.add(event4);

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
  public void createMultiple() throws JsonProcessingException {
    List<Event> events = createMockEventsForTraceTest();

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

    assertEquals(4, events.size());

  }

  @Test
  @Order(40)
  public void searchBySpecificTraceId()
  {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ignored) {}

    EventParams params = EventParams.builder().condition(
            and(field("trace.uuid", EVENT_TRACE_ID))
    ).build();

    //Field field = field("trace.id", EVENT_TRACE_ID);
    //Condition c = and(field);


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

            //this.validate(events);
    assertEquals(EVENT_TRACE_ID, events.get(0).getTrace().getId());
    assertEquals(3, events.size());
  }

  @Test
  @Order(45)
  public void searchAllEventsWithoutSpecificTraceId()
  {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ignored) {}

    EventParams params =  EventParams.builder().condition(
            not(field("trace.uuid", EVENT_TRACE_ID))
    ).build();

    params.tryGetResultFilter().setSize(100);
    params.tryGetResultFilter().getContent().setTreeLevel(5);

    List<Event> events =  given()
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

    assertNotEquals(EVENT_TRACE_ID, events.get(0).getTrace().getId());
    assertEquals(1, events.size());
  }

  @Test
  @Order(50)
  public void searchEventsBySpecificId()
  {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ignored) {}

    EventParams params = EventParams.builder().condition(
            and(field("uuid", "6b829aac-06d2-4cbc-9721-d6d24a3628dd"))
    ).build();

    params.tryGetResultFilter().setSize(100);
    params.tryGetResultFilter().getContent().setTreeLevel(5);

    List<Event> events =  given()
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

    assertEquals("6b829aac-06d2-4cbc-9721-d6d24a3628dd", events.get(0).getId());
    assertEquals(1, events.size());

  }

  @Test
  @Order(55)
  public void searchEventsByOrOperator()
  {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ignored) {}

    EventParams params = EventParams.builder().condition(
         or(
                 field("trace.uuid", EVENT_TRACE_ID),
                 field("unit", "Mock_Unit")
         )
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

    assertFalse(events.isEmpty());

    for (Event event : events) {
      boolean matchesCondition = EVENT_TRACE_ID.equals(event.getTrace().getId()) ||
              "Mock_Unit".equals(event.getUnit());

      assertTrue(matchesCondition, String.format("Event did not match any condition: %s", event));
    }

    assertEquals("Mock_Unit", events.get(0).getUnit());

  }

  @Test
  @Order(60)
  public void searchEventsWithMultipleFilters() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ignored) {}

    EventParams params = EventParams.builder().condition(
            and(
                field(
                   "created",
                        gteLt(
                                Iso8601.fromIso("2023-01-01T12:12:12.12"),
                                Iso8601.fromIso("2027-01-01T12:12:12.12")
                        )
                ),
                field("trace.uuid", EVENT_TRACE_ID),
                field("unit", "Mock_Unit")
            )
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

    assertEquals(3, events.size());

  }

  /*@Test
  @Order(65)
  public void searchEventsByGreaterDate()
  {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ignored) {}

    EventParams params = EventParams.builder().condition(
            and(
                    field(
                            "created",
                            gt(Iso8601.fromIso("2024-01-01T00:00:00Z"))
                    )
            )
    ).build();



  }*/

}
