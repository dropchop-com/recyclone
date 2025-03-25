package com.dropchop.recyclone.quarkus.it.rest.events;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.dropchop.recyclone.base.dto.model.event.Event;
import com.dropchop.recyclone.base.dto.model.event.EventDetail;
import com.dropchop.recyclone.base.dto.model.event.EventItem;
import com.dropchop.recyclone.base.dto.model.event.EventTrace;
import com.dropchop.recyclone.base.dto.model.invoke.EventParams;
import com.dropchop.recyclone.quarkus.it.rest.events.mock.EventMockData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

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
  EventMockData eventMockData;

  @BeforeEach
  public void setUp() {
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
      objectMapperConfig().jackson2ObjectMapperFactory((type, s) -> mapper)
    );
  }

  public static String EVENT_ID = "feea39e2-aea0-4395-8be3-dd42ca42f03c";
  public static String EVENT_DETAIL_ID = "eebd0fda-9e81-4fa8-a4c6-d3cdbc06e4c8";
  public static String EVENT_TRACE_NAME = "some_test_flow";

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
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getSource().getSubject().getName());
    assertNotNull(rspEvent.getSource().getObject());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getSource().getObject().getName());
    assertNotNull(rspEvent.getSource().getService());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getSource().getService().getName());
    assertNotNull(rspEvent.getSource().getContext());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getSource().getContext().getName());
    assertNotNull(rspEvent.getCause());
    assertNotNull(rspEvent.getCause().getSubject());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getCause().getSubject().getName());
    assertNotNull(rspEvent.getCause().getObject());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getCause().getObject().getName());
    assertNotNull(rspEvent.getCause().getService());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getCause().getService().getName());
    assertNotNull(rspEvent.getCause().getContext());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getCause().getContext().getName());
    assertNotNull(rspEvent.getTarget());
    assertNotNull(rspEvent.getTarget().getSubject());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getTarget().getSubject().getName());
    assertNotNull(rspEvent.getTarget().getObject());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getTarget().getObject().getName());
    assertNotNull(rspEvent.getTarget().getService());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getTarget().getService().getName());
    assertNotNull(rspEvent.getTarget().getContext());
    assertEquals(Strings.EVENT_DETAIL, rspEvent.getTarget().getContext().getName());
    assertNotNull(rspEvent.getTrace());
    assertEquals(EVENT_TRACE_NAME, rspEvent.getTrace().getName());
    assertEquals(Strings.CONTEXT, rspEvent.getTrace().getContext());
    assertEquals(Strings.GROUP, rspEvent.getTrace().getGroup());

      /*assertNotNull(rspEvent.getAttributeValue(Strings.ATTRIBUTE_STR));
    assertEquals(Strings.ATTRIBUTE_STR, rspEvent.getAttributeValue(Strings.ATTRIBUTE_STR));
    assertEquals(1, ((BigDecimal)rspEvent.getAttributeValue(Strings.ATTRIBUTE_NUM)).intValue());
    assertEquals(true, rspEvent.getAttributeValue(Strings.ATTRIBUTE_BOOL));*/

  }

  @Test
  @Order(10)
  public void create() {

    EventDetail detail = new EventDetail();
    detail.setId(UUID.randomUUID().toString());
    detail.setId(EVENT_DETAIL_ID);
    detail.setName(Strings.EVENT_DETAIL);

    EventItem eventItem = new EventItem();
    eventItem.setSubject(detail);
    eventItem.setObject(detail);
    eventItem.setService(detail);
    eventItem.setContext(detail);

    EventTrace eventTrace = new EventTrace();
    eventTrace.setName(EVENT_TRACE_NAME);
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
    } catch (InterruptedException ignored) {
    }

    Condition c = or(
      field("uuid", EVENT_ID),
      and(
        field("created", ZonedDateTime.now())
      )
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
    } catch (InterruptedException ignored) {
    }

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
    } catch (InterruptedException ignored) {
    }

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
  public void createMultiple() throws JsonProcessingException {
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

  }

  @Test
  @Order(40)
  public void searchBySpecificTraceId() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ignored) {
    }

    /*
     * Search by specific trace uuid
     * */
    EventParams params = EventParams.builder().condition(
      and(field("trace.uuid", EVENT_TRACE_NAME))
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
    assertEquals(EVENT_TRACE_NAME, events.get(0).getTrace().getName());
    assertEquals(3, events.size());
  }

  @Test
  @Order(45)
  public void searchAllEventsWithoutSpecificTraceId() {
    /*
     * Find all events except the one with this specific EVENT_TRACE_ID
     * */
    EventParams params = EventParams.builder().condition(
      not(field("trace.uuid", EVENT_TRACE_NAME))
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

    assertNotEquals(EVENT_TRACE_NAME, events.get(0).getTrace().getName());
    assertEquals(1, events.size());
  }

  @Test
  @Order(50)
  public void searchEventsBySpecificId() {

    /*
     * Find events:
     *  by uuid
     * */
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

    assertEquals("6b829aac-06d2-4cbc-9721-d6d24a3628dd", events.get(0).getId());
    assertEquals(1, events.size());

  }

  @Test
  @Order(55)
  public void searchEventsByOrOperator() {

    /*
     * Find events:
     * where uuid is EVENT_TRACE_ID
     * or
     * unit field has a value of "Mock_Unit"
     * */
    EventParams params = EventParams.builder().condition(
      or(
        field("trace.uuid", EVENT_TRACE_NAME),
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
      boolean matchesCondition = EVENT_TRACE_NAME.equals(event.getTrace().getName()) ||
        "Mock_Unit".equals(event.getUnit());

      assertTrue(matchesCondition, String.format("Event did not match any condition: %s", event));
    }

    assertEquals("Mock_Unit", events.get(0).getUnit());

  }

  @Test
  @Order(60)
  public void searchEventsWithMultipleFilters() {

    /*
     * Find events:
     * between dates and uuid must be EVENT_TRACE_ID
     * */
    EventParams params = EventParams.builder().condition(
      and(
        field(
          "created",
          gteLt(
            Iso8601.fromIso("2023-01-01T12:12:12.12"),
            Iso8601.fromIso("2027-01-01T12:12:12.12")
          )
        ),
        field("trace.name", EVENT_TRACE_NAME)
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
  public void searchEventsByDateRange() {

    /*
     * Find events:
     * between dates
     * */
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
  public void searchEventsByDateRange2() {

    ZonedDateTime startDate = Iso8601.fromIso("2024-01-01T12:12:15.20");
    ZonedDateTime endDate = Iso8601.fromIso("2030-01-01T12:12:12.12");

    /*
     * Find events:
     * between dates
     * */
    EventParams params = EventParams.builder().condition(
      and(
        field(
          "created",
          gtLt(
            /*
             * Hour dependend (check event object to see the comparison)
             * */
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
  public void searchEventsByDateRange3() {

    ZonedDateTime startDate = Iso8601.fromIso("2024-01-01T00:01:10.20");
    ZonedDateTime endDate = Iso8601.fromIso("2024-06-15T23:31:45.50");

    /*
     * Find events:
     * where created is between both date values
     * same as before just with different dates
     * */
    EventParams params = EventParams.builder().condition(
      and(
        field(
          "created",
          gtLt(
            /*
             * Hour dependend (check event object to see the comparison)
             * */
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
            "created",
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
            "created",
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
            "created",
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
