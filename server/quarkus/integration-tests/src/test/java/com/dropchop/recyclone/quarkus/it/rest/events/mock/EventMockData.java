package com.dropchop.recyclone.quarkus.it.rest.events.mock;

import com.dropchop.recyclone.base.dto.model.event.Event;
import com.dropchop.recyclone.base.dto.model.event.EventDetail;
import com.dropchop.recyclone.base.dto.model.event.EventItem;
import com.dropchop.recyclone.base.dto.model.event.EventTrace;
import io.quarkus.test.Mock;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mock
public class EventMockData {

    public static String EVENT_TRACE_NAME = "some_test_flow";
    public static String EVENT_TRACE_NAME2 = "some_test_flow_2";

    public List<Event> createMockEvents()
    {
        List<Event> mockEvents = new ArrayList<>();
        Event event = new Event();
        event.setId("4794b019-9750-44f4-a3c9-33516c6bfc50");
        event.setApplication("Baucheck-backend");
        event.setType("Backend");
        event.setAction("FETCH_DATA");

        ZonedDateTime created = ZonedDateTime.of(2024,1,1,0,1,15, 20, ZoneId.of("UTC"));

        EventItem source = createMockEventItem(
                createMockEventDetailSubject("d7e5ad61-0a9d-4493-ad73-a3e9a9d42999",null, "Source_Subject", null, null, null),
                createMockEventDetailObject("96fa1d85-c1c1-4f06-b7d7-858ef70002b3",null ,"Source_Object", null, null ,null),
                createMockEventDetailService("c3b12126-7bf5-4ce1-955c-db73df6e5bd6",null ,"Source_Service", null, null ,null),
                createMockEventDetailContext("f8dea019-a47e-4652-b3f1-69f7ebd610b3",null ,"Source_Context", null, null ,null)
        );
        EventItem target = createMockEventItem(
                createMockEventDetailSubject("d7e5ad61-0a9d-4493-ad73-a3e9a9d42999",null ,"Target_Subject", null, null ,null),
                createMockEventDetailObject("96fa1d85-c1c1-4f06-b7d7-858ef70002b3",null ,"Target_Object", null, null ,null),
                createMockEventDetailService("c3b12126-7bf5-4ce1-955c-db73df6e5bd6",null ,"Target_Service", null, null ,null),
                createMockEventDetailContext("f8dea019-a47e-4652-b3f1-69f7ebd610b3",null ,"Target_Context", null, null ,null)
        );
        EventItem cause = createMockEventItem(
                createMockEventDetailSubject("d7e5ad61-0a9d-4493-ad73-a3e9a9d42999",null ,"Cause_Subject", null, null ,null),
                createMockEventDetailObject("96fa1d85-c1c1-4f06-b7d7-858ef70002b3",null ,"Cause_Object", null, null ,null),
                createMockEventDetailService("c3b12126-7bf5-4ce1-955c-db73df6e5bd6",null ,"Cause_Service", null, null ,null),
                createMockEventDetailContext("f8dea019-a47e-4652-b3f1-69f7ebd610b3",null ,"Cause_Context", null, null ,null)
        );

        EventTrace trace = createMockEventTrace(EVENT_TRACE_NAME,"Mock_Group", "Mock_Context");

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
                createMockEventDetailSubject("9579ba79-282e-4d24-b2a6-6b7c23cdc9a1",null, "Source_Subject2", null, null ,null),
                createMockEventDetailObject("e445b717-6cad-4218-aefb-ca2b4ecf6cd4",null, "Source_Object2", null, null ,null),
                createMockEventDetailService("a4714d43-f459-4f32-b0f4-1b09fc345c6e",null, "Source_Service2", null, null ,null),
                createMockEventDetailContext("5e5aad1a-6b53-4311-81cf-82e7a5b5dda1",null, "Source_Context2", null, null ,null)
        );
        EventItem target2 = createMockEventItem(
                createMockEventDetailSubject("ee76f4b4-33a0-4e86-8605-334ee48e2df9",null, "Target_Subject2", null, null ,null),
                createMockEventDetailObject("e9db69e0-42a0-4b3c-9d2e-00ff47b88288",null, "Target_Object2", null, null ,null),
                createMockEventDetailService("01101d6a-1c81-4b73-a0ec-d58d38bbdc7f",null, "Target_Service2", null, null ,null),
                createMockEventDetailContext("e3a17276-aa58-46c5-be7c-5888dd1ba13e",null, "Target_Context2", null, null ,null)
        );
        EventItem cause2 = createMockEventItem(
                createMockEventDetailSubject("ce5e1c87-06fd-49bd-8012-09d4f9544aad",null, "Cause_Subject2", null, null ,null),
                createMockEventDetailObject("858e731e-c98d-45f8-8a11-b794b7fadbf5",null, "Cause_Object2", null, null ,null),
                createMockEventDetailService("0bbd008a-fda5-49f5-9cfc-e87cf37c6c3e",null, "Cause_Service2", null, null ,null),
                createMockEventDetailContext("d0644ca7-80fb-4560-af63-8b1e93e5e1d3",null, "Cause_Context2", null, null ,null)
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
                createMockEventDetailSubject("b92e8daa-b240-46dc-826a-e14b9723fc92",null, "Source_Subject3", null, null, null),
                createMockEventDetailObject("e4212462-4b1a-498b-9a69-5eb85adae2aa",null, "Source_Object3", null, null, null),
                createMockEventDetailService("76e4dcef-ebe5-4d54-bbe1-5febddb2991b",null, "Source_Service3", null, null, null),
                createMockEventDetailContext("82b62754-b9f9-4e5a-82dc-0bfeb58d113b",null, "Source_Context3", null, null, null)
        );
        EventItem target3 = createMockEventItem(
                createMockEventDetailSubject("b92e8daa-b240-46dc-826a-e14b9723fc92", null, "Target_Subject3",null, null, null),
                createMockEventDetailObject("e4212462-4b1a-498b-9a69-5eb85adae2aa",null, "Target_Object3", null, null, null),
                createMockEventDetailService("76e4dcef-ebe5-4d54-bbe1-5febddb2991b",null, "Target_Service3", null, null, null),
                createMockEventDetailContext("82b62754-b9f9-4e5a-82dc-0bfeb58d113b",null, "Target_Context3", null, null, null)
        );
        EventItem cause3 = createMockEventItem(
                createMockEventDetailSubject("b92e8daa-b240-46dc-826a-e14b9723fc92",null, "Cause_Subject3", null, null, null),
                createMockEventDetailObject("e4212462-4b1a-498b-9a69-5eb85adae2aa",null, "Cause_Object3", null, null, null),
                createMockEventDetailService("76e4dcef-ebe5-4d54-bbe1-5febddb2991b",null, "Cause_Service3", null, null, null),
                createMockEventDetailContext("82b62754-b9f9-4e5a-82dc-0bfeb58d113b",null, "Cause_Context3", null, null, null)
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
                createMockEventDetailSubject("d547869d-0ca4-41c5-a8fd-54ee58934a82",null, "Source_Subject4",null,  null, null),
                createMockEventDetailObject("dc654f9d-1138-485e-9d8f-1caf6eed4591",null, "Source_Object4",null,  null, null),
                createMockEventDetailService("f37f308f-5649-4f85-a9c8-e89c850a2e2a",null, "Source_Service4", null, null, null),
                createMockEventDetailContext("1e17af47-2875-4766-bb77-73e340ded8b8",null, "Source_Context4", null, null, null)
        );
        EventItem target4 = createMockEventItem(
                createMockEventDetailSubject("d547869d-0ca4-41c5-a8fd-54ee58934a82",null, "Target_Subject4", null, null, null),
                createMockEventDetailObject("dc654f9d-1138-485e-9d8f-1caf6eed4591",null, "Target_Object4", null, null, null),
                createMockEventDetailService("f37f308f-5649-4f85-a9c8-e89c850a2e2a",null, "Target_Service4",null,  null, null),
                createMockEventDetailContext("1e17af47-2875-4766-bb77-73e340ded8b8",null, "Target_Context4", null, null, null)
        );
        EventItem cause4 = createMockEventItem(
                createMockEventDetailSubject("d547869d-0ca4-41c5-a8fd-54ee58934a82",null, "Cause_Subject4", null, null, null),
                createMockEventDetailObject("dc654f9d-1138-485e-9d8f-1caf6eed4591",null, "Cause_Object4", null, null, null),
                createMockEventDetailService("f37f308f-5649-4f85-a9c8-e89c850a2e2a",null, "Cause_Service4", null, null, null),
                createMockEventDetailContext("1e17af47-2875-4766-bb77-73e340ded8b8",null, "Cause_Context4", null, null, null)
        );
        EventTrace trace2 = createMockEventTrace(EVENT_TRACE_NAME2, "Some group", "Some context");

        event4.setSource(source4);
        event4.setTarget(target4);
        event4.setCause(cause4);
        event4.setTrace(trace2); //we use different trace because we want to exclude some events for query testing purposes
        event4.setCreated(created4);
        event4.setValue(3.0);
        event4.setUnit("Mock_Unit");

        mockEvents.add(event4);

        return mockEvents;
    }

    private EventDetail createMockEventDetail(String id, String descriptor, String name, String value, EventDetail parent, EventDetail child) {
        EventDetail detail = new EventDetail();
        detail.setId(id);
        detail.setDescriptor(descriptor);
        detail.setName(name);
        detail.setValue(value);
        detail.setParent(parent);
        detail.setChild(child);

        return detail;

    }

    private EventItem createMockEventItem(EventDetail subject, EventDetail object, EventDetail service, EventDetail context)
    {
        EventItem item = new EventItem();
        item.setSubject(subject);
        item.setObject(object);
        item.setService(service);
        item.setContext(context);

        return item;
    }

    private EventTrace createMockEventTrace(String name,String group, String context)
    {
        EventTrace trace = new EventTrace();
        trace.setName(name);
        trace.setGroup(group);
        trace.setContext(context);
        return trace;
    }

    private EventDetail createMockEventDetailSubject(String id, String descriptor, String name, String value,EventDetail parent, EventDetail child) {
        return createMockEventDetail(id, descriptor, name, value, parent, child);
    }

    private EventDetail createMockEventDetailObject(String id, String descriptor, String name, String value,EventDetail parent, EventDetail child)
    {
        return createMockEventDetail(id, descriptor, name, value, parent, child);
    }

    private EventDetail createMockEventDetailService(String id, String descriptor, String name, String value,EventDetail parent, EventDetail child)
    {
        return createMockEventDetail(id, descriptor, name, value, parent, child);
    }

    private EventDetail createMockEventDetailContext(String id, String descriptor, String name, String value,EventDetail parent, EventDetail child)
    {
        return createMockEventDetail(id, descriptor, name, value, parent, child);
    }
}
