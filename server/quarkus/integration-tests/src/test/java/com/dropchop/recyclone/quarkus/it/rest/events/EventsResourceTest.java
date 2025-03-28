package com.dropchop.recyclone.quarkus.it.rest.events;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.dropchop.recyclone.base.api.model.utils.Uuid;
import com.dropchop.recyclone.base.dto.model.base.DtoId;
import com.dropchop.recyclone.base.dto.model.event.Event;
import com.dropchop.recyclone.base.dto.model.event.EventDetail;
import com.dropchop.recyclone.base.dto.model.event.EventItem;
import com.dropchop.recyclone.base.dto.model.event.EventTrace;
import com.dropchop.recyclone.base.dto.model.invoke.EventParams;
import com.dropchop.recyclone.base.dto.model.invoke.ResultFilter;
import com.dropchop.recyclone.quarkus.it.rest.events.mock.EventMockData;
import com.dropchop.recyclone.quarkus.runtime.elasticsearch.ElasticSearchTestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.ZonedDateTime;
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

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  EventMockData eventMockData;

  @Inject
  ElasticSearchTestHelper testHelper;

  @BeforeEach
  public void setUp() {
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
      objectMapperConfig().jackson2ObjectMapperFactory((type, s) -> mapper)
    );
  }

  public static String EVENT_ID = Uuid.getTimeBased().toString();
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

  private void validateNonNested(List<Event> events) {
    Event rspEvent = events.getFirst();
    assertEquals(EVENT_ID, rspEvent.getId());
    assertNotNull(rspEvent.getCreated());
    assertEquals(Strings.APPLICATION, rspEvent.getApplication());
    assertEquals(Strings.TYPE, rspEvent.getType());
    assertEquals(Strings.ACTION, rspEvent.getAction());
    assertEquals(Strings.DATA, rspEvent.getData());
    //assertEquals(rspEvent.getValue(), rspEvent.getValue());
    assertEquals(Strings.UNIT, rspEvent.getUnit());
  }

  private void validate(List<Event> events) {
    validateNonNested(events);
    Event rspEvent = events.getFirst();
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

  @Test
  @Order(10)
  @Tag("create")
  @Tag("delete")
  @Tag("delete")
  public void create() throws IOException {

    EventDetail detail = new EventDetail();
    detail.setId(UUID.randomUUID().toString());
    detail.setCreated(ZonedDateTime.now());
    detail.setId(EVENT_DETAIL_ID);
    detail.setName(Strings.EVENT_DETAIL);

    EventItem eventItem = new EventItem();
    eventItem.setId(UUID.randomUUID().toString());
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
      .post("/api/internal/events")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Event.class);
    this.validateNonNested(events);
    testHelper.waitForObject("/event/_search",  EVENT_ID);
  }

  @Test
  @Order(20)
  public void search() {
    Condition c = or(
      field("uuid", EVENT_ID),
      and(
        field("created", ZonedDateTime.now())
      )
    );

    EventParams params = EventParams
        .builder()
        .filter(
            new ResultFilter()
                .content(new ResultFilter.ContentFilter().treeLevel(5))
                .size(100)
        )
        .condition(c)
        .build();

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
    List<Event> events = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .auth().preemptive().basic("admin1", "password")
      .and()
      .when()
      .get("/api/internal/events/" + EVENT_ID + "?c_level=5")
      .then()
      .statusCode(200)
      .extract()
      .body().jsonPath().getList(".", Event.class);

    this.validate(events);

  }

  @Test
  @Order(30)
  @Tag("delete")
  public void delete() throws IOException {
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
    testHelper.waitForObjects("/event/_search", EVENT_ID, 0);


    Condition c = or(
        field("uuid", EVENT_ID)
    );

    EventParams params = EventParams.builder().condition(c).build();
    params.tryGetResultFilter().setSize(100);
    params.tryGetResultFilter().getContent().setTreeLevel(5);


    events = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(MediaType.APPLICATION_JSON)
      .header("Authorization", "Bearer editortoken1")
      .auth().preemptive().basic("admin1", "password")
      .and()
      .body(params)
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
  @Tag("createMultiple")
  @Tag("searchBySpecificTraceId")
  @Tag("searchAllEventsWithoutSpecificTraceId")
  @Tag("searchEventsBySpecificId")
  @Tag("searchEventsByTraceIdOrUnitString")
  @Tag("searchEventsInPeriodWithTraceId")
  @Tag("searchEventsByDateRange")
  @Tag("searchEventsByDateRange2")
  @Tag("searchEventsByDateRange3")
  @Tag("complexQuery")
  @Tag("complexQuery2")
  @Tag("complexQuery3")
  @Tag("complexQuery4")
  @Tag("complexQuery5")
  public void createMultiple() throws IOException {
    //List<Event> events = createMockEventsForTraceTest();
    List<Event> events = eventMockData.createMockEvents();

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
    List<String> eIds = events.stream().map(DtoId::getId).toList();
    testHelper.waitForObjects("/event/_search", eIds, 4);
  }

  @Test
  @Order(40)
  @Tag("searchBySpecificTraceId")
  public void searchBySpecificTraceId() {
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
    assertEquals(EVENT_TRACE_ID, events.getFirst().getTrace().getId());
    assertEquals(3, events.size());
  }

  @Test
  @Order(45)
  @Tag("searchAllEventsWithoutSpecificTraceId")
  public void searchAllEventsWithoutSpecificTraceId() {
    EventParams params = EventParams.builder().condition(
      not(field("trace.uuid", EVENT_TRACE_ID))
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

    assertNotEquals(EVENT_TRACE_ID, events.getFirst().getTrace().getId());
    assertEquals(1, events.size());
  }

  @Test
  @Order(50)
  @Tag("searchEventsBySpecificId")
  public void searchEventsBySpecificId() {
    EventParams params = EventParams.builder().condition(
      and(field("uuid", "6b829aac-06d2-4cbc-9721-d6d24a3628dd"))
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

    assertEquals("6b829aac-06d2-4cbc-9721-d6d24a3628dd", events.getFirst().getId());
    assertEquals(1, events.size());
  }

  @Test
  @Order(55)
  @Tag("searchEventsByTraceIdOrUnitString")
  public void searchEventsByTraceIdOrUnitString() {
    EventParams params = EventParams.builder().condition(
      or(
        field("trace.uuid", EVENT_TRACE_ID),
        field("unit", "Mock_Unit") //isnt actually used until we implement MATCH type search
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

    assertEquals("Mock_Unit", events.getFirst().getUnit());
  }

  @Test
  @Order(60)
  @Tag("searchEventsInPeriodWithTraceId")
  public void searchEventsInPeriodWithTraceId() {
    EventParams params = EventParams.builder().condition(
      and(
        field(
          "created",
          gteLt(
            Iso8601.fromIso("2023-01-01T12:12:12.12"),
            Iso8601.fromIso("2027-01-01T12:12:12.12")
          )
        ),
        field("trace.uuid", EVENT_TRACE_ID)
        //field("unit", "Mock_Unit") /*need to add support to handle "text" fields*/
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

  @Test
  @Order(65)
  @Tag("searchEventsByDateRange")
  public void searchEventsByDateRange() {
    EventParams params = EventParams.builder().condition(
      and(
        field(
          "created",
          gteLt(
            Iso8601.fromIso("2023-01-01T12:12:12.12"),
            Iso8601.fromIso("2030-01-01T12:12:12.12")
          )
        )
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
    assertEquals(4, events.size());
  }

  @Test
  @Order(70)
  @Tag("searchEventsByDateRange2")
  public void searchEventsByDateRange2() {
    ZonedDateTime startDate = Iso8601.fromIso("2024-01-01T12:12:15.20");
    ZonedDateTime endDate = Iso8601.fromIso("2030-01-01T12:12:12.12");
    EventParams params = EventParams.builder().condition(
      and(
        field(
          "created",
          gtLt(
            startDate,
            endDate
          )
        )
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

    for (Event event : events) {
      ZonedDateTime createdDate = event.getCreated();
      assertTrue(
        createdDate.isAfter(startDate) && createdDate.isBefore(endDate),
        "Event date is out of range: " + createdDate
      );
    }
  }

  @Test
  @Order(75)
  @Tag("searchEventsByDateRange3")
  public void searchEventsByDateRange3() {
    ZonedDateTime startDate = Iso8601.fromIso("2024-01-01T00:01:10.20");
    ZonedDateTime endDate = Iso8601.fromIso("2024-06-15T23:31:45.50");
    EventParams params = EventParams.builder().condition(
      and(
        field(
          "created",
          gtLt(
            startDate,
            endDate
          )
        )
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

    assertEquals(2, events.size());

    for (Event event : events) {
      ZonedDateTime createdDate = event.getCreated();
      assertTrue(
        createdDate.isAfter(startDate) && createdDate.isBefore(endDate),
        "Event date is out of range: " + createdDate
      );
    }

  }

  @Test
  @Order(80)
  @Tag("complexQuery")
  public void complexQuery() {
    ZonedDateTime startDate = Iso8601.fromIso("2024-01-01T00:01:10.20");
    ZonedDateTime endDate = Iso8601.fromIso("2024-06-15T23:31:45.50");

    /*
     * Find all events:
     * type must be either "Backend" or "frontend"
     * and
     * value in [1.0, 2.0]
     * and
     * either between the date or trace.group must have "Some group value"
     * */
    EventParams params = EventParams.builder().condition(
      and(
        or(
          field(
            "target.created",
            gteLt(
              startDate,
              endDate
            )
          ),
          field(
            "trace.group",
            "Some group"
          )
        )
      ).and(
        field("type", in("Backend", "frontend"))
      ).and(
        field("value", in(1.0, 2.0))
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

    assertEquals(2, events.size());

    for (Event event : events) {
      assertTrue((event.getValue() == 1.0 || event.getValue() == 2.0) &&
        event.getType().equals("Backend") || event.getType().equals("frontend"));
    }

  }

  @Test
  @Order(85)
  @Tag("complexQuery2")
  public void complexQuery2() {
    ZonedDateTime startDate = Iso8601.fromIso("2025-01-01T00:01:10.20");
    ZonedDateTime endDate = Iso8601.fromIso("2026-06-15T23:31:45.50");
    ZonedDateTime startDate2 = Iso8601.fromIso("2024-06-14T00:00:00.000");
    ZonedDateTime endDate2 = Iso8601.fromIso("2024-06-17T00:00:00.000");
    /*
     * Find all the events:
     * that container either of the field value (target.created or trace.group..)
     * and
     * type field must be either "frontend" or "Backend"
     * and
     * value field must be in: 1.0, 2.0, 3.0
     * */
    EventParams params = EventParams.builder().condition(
      and(
        or(
          field(
            "target.created",
            gteLt(
              startDate,
              endDate
            )
          ),
          field(
            "trace.group",
            "Some group"
          ),
          field(
            "uuid", "4794b019-9750-44f4-a3c9-33516c6bfc50"
          ),
          /*
           * Not gonna work because "match" search is not implemented yet
           *
           *field("target.subject.name", "Target_Subject2"),
           */
          field(
            "target.created",
            gteLt(
              startDate2,
              endDate2
            )
          )
        )
      ).and(
        field("type", in("Backend", "frontend"))
      ).and(
        field("value", in(1.0, 2.0, 3.0))
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

  @Test
  @Order(90)
  @Tag("complexQuery3")
  public void complexQuery3() {

    /*
     * Find every record that contains either ("Baucheck-backend", "Backend", "FETCH_DATA")
     * or ("Lupitpole-frontend", "frontend", "SUBMIT_FORM_FRONTEND")
     * */
    EventParams params = EventParams.builder().condition(
      or(
        and(
          field("application", "Baucheck-backend"),
          field("type", "Backend"),
          field("action", "FETCH_DATA")
        )
      ).or(
        and(
          field("application", "Lupitpole-frontend"),
          field("type", "frontend"),
          field("action", "SUBMIT_FORM_FRONTEND")
        )
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

    assertEquals(2, events.size());

  }

  @Test
  @Order(95)
  @Tag("complexQuery4")
  public void complexQuery4() {

    ZonedDateTime startDate = Iso8601.fromIso("2025-01-01T00:01:10.20");
    ZonedDateTime endDate = Iso8601.fromIso("2026-06-15T23:31:45.50");

    /*
     * Description:
     * Find all the events that:
     * were created between startDate and endDate or:
     * event contains particular value combination:
     * ("Baucheck-backend", "Backend", "FETCH_DATA") or ("Lupitpole-frontend", "frontend", "SUBMIT_FORM_FRONTEND")
     * */
    EventParams params = EventParams.builder().condition(
      and(
        or(
          and(
            field("application", "Baucheck-backend"),
            field("type", "Backend"),
            field("action", "FETCH_DATA")
          )
        ).or(
          and(
            field("application", "Lupitpole-frontend"),
            field("type", "frontend"),
            field("action", "SUBMIT_FORM_FRONTEND")
          )
        ),
        and(
          field("cause.created",
            gtLt(
              startDate,
              endDate
            )
          )
        )

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

    assertEquals(1, events.size());

  }

  @Test
  @Order(100)
  @Tag("complexQuery5")
  public void complexQuery5() {
    ZonedDateTime startDate = Iso8601.fromIso("2025-01-01T00:01:10.20");
    ZonedDateTime endDate = Iso8601.fromIso("2026-06-15T23:31:45.50");


    /*
     * Description:
     * find events that:
     * were not created between startDate and EndDate and the uuid should not be ... or
     * event should contain FETCH_DATA in the action field
     * */
    EventParams params = EventParams.builder().condition(
      or(
        and(
          not(
            field(
              "created",
              gteLte(
                startDate,
                endDate
              )
            )
          ),
          not(
            field(
              "uuid",
              "4794b019-9750-44f4-a3c9-33516c6bfc50"
            )
          )
        ),
        field(
          "action", "FETCH_DATA"
        )
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

    assertEquals(2, events.size());
  }


}
