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

    public static String EVENT_TRACE_ID = "6df37bd3-073f-45f2-9f00-65022f2b4019";

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
                "1394a7aa-184c-4901-9a86-112e369f95aa",
                "source",
                createMockEventDetailSubject("d7e5ad61-0a9d-4493-ad73-a3e9a9d42999","Source_Subject", null, null, created),
                createMockEventDetailObject("96fa1d85-c1c1-4f06-b7d7-858ef70002b3","Source_Object", null, null, created),
                createMockEventDetailService("c3b12126-7bf5-4ce1-955c-db73df6e5bd6","Source_Service", null, null, created),
                createMockEventDetailContext("f8dea019-a47e-4652-b3f1-69f7ebd610b3","Source_Context", null, null, created),
                created
        );
        EventItem target = createMockEventItem(
                "cb7113f1-2bf3-4e23-ac4b-3f65b2ca10d9",
                "Target",
                createMockEventDetailSubject("d7e5ad61-0a9d-4493-ad73-a3e9a9d42999","Target_Subject", null, null, created),
                createMockEventDetailObject("96fa1d85-c1c1-4f06-b7d7-858ef70002b3","Target_Object", null, null, created),
                createMockEventDetailService("c3b12126-7bf5-4ce1-955c-db73df6e5bd6","Target_Service", null, null, created),
                createMockEventDetailContext("f8dea019-a47e-4652-b3f1-69f7ebd610b3","Target_Context", null, null, created),
                created
        );
        EventItem cause = createMockEventItem(
            "fd210584-ccab-42c3-b0cc-51ddb1e54309",
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
                "ba69a798-a7cb-430d-93ed-fe3e5c16836e",
                "source",
                createMockEventDetailSubject("9579ba79-282e-4d24-b2a6-6b7c23cdc9a1","Source_Subject2", null, null,created2),
                createMockEventDetailObject("e445b717-6cad-4218-aefb-ca2b4ecf6cd4","Source_Object2", null, null,created2),
                createMockEventDetailService("a4714d43-f459-4f32-b0f4-1b09fc345c6e","Source_Service2", null, null,created2),
                createMockEventDetailContext("5e5aad1a-6b53-4311-81cf-82e7a5b5dda1","Source_Context2", null, null,created2),
                created2
        );
        EventItem target2 = createMockEventItem(
                "8ab165ee-463a-4f81-9c3d-46c45842667f",
                "Target",
                createMockEventDetailSubject("ee76f4b4-33a0-4e86-8605-334ee48e2df9","Target_Subject2", null, null,created2),
                createMockEventDetailObject("e9db69e0-42a0-4b3c-9d2e-00ff47b88288","Target_Object2", null, null,created2),
                createMockEventDetailService("01101d6a-1c81-4b73-a0ec-d58d38bbdc7f","Target_Service2", null, null,created2),
                createMockEventDetailContext("e3a17276-aa58-46c5-be7c-5888dd1ba13e","Target_Context2", null, null,created2),
                created2
        );
        EventItem cause2 = createMockEventItem(
                "024c2c73-3bf1-4910-b292-aaa38c3d78d4",
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
                "a508c67d-0eeb-4b4b-9893-3ffddeb90b37",
                "source",
                createMockEventDetailSubject("b92e8daa-b240-46dc-826a-e14b9723fc92","Source_Subject3", null, null,created3),
                createMockEventDetailObject("e4212462-4b1a-498b-9a69-5eb85adae2aa","Source_Object3", null, null,created3),
                createMockEventDetailService("76e4dcef-ebe5-4d54-bbe1-5febddb2991b","Source_Service3", null, null,created3),
                createMockEventDetailContext("82b62754-b9f9-4e5a-82dc-0bfeb58d113b","Source_Context3", null, null,created3),
                created3
        );
        EventItem target3 = createMockEventItem(
                "b8ff42c9-915c-4f04-b401-08eef3736011",
                "Target",
                createMockEventDetailSubject("b92e8daa-b240-46dc-826a-e14b9723fc92", "Target_Subject3",null, null,created3),
                createMockEventDetailObject("e4212462-4b1a-498b-9a69-5eb85adae2aa","Target_Object3", null, null,created3),
                createMockEventDetailService("76e4dcef-ebe5-4d54-bbe1-5febddb2991b","Target_Service3", null, null,created3),
                createMockEventDetailContext("82b62754-b9f9-4e5a-82dc-0bfeb58d113b","Target_Context3", null, null,created3),
                created3
        );
        EventItem cause3 = createMockEventItem(
                "4013f96d-14c9-4c28-94f5-1065ae62ef46",
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
                "c7e3ae6f-1d6f-438c-992d-c1095c6f1cb1",
                "source",
                createMockEventDetailSubject("d547869d-0ca4-41c5-a8fd-54ee58934a82","Source_Subject4", null, null,created4),
                createMockEventDetailObject("dc654f9d-1138-485e-9d8f-1caf6eed4591","Source_Object4", null, null,created4),
                createMockEventDetailService("f37f308f-5649-4f85-a9c8-e89c850a2e2a","Source_Service4", null, null,created4),
                createMockEventDetailContext("1e17af47-2875-4766-bb77-73e340ded8b8","Source_Context4", null, null,created4),
                created4
        );
        EventItem target4 = createMockEventItem(
                "17e41c93-ecfb-4a78-a1a2-914a3ca84868",
                "Target",
                createMockEventDetailSubject("d547869d-0ca4-41c5-a8fd-54ee58934a82","Target_Subject4", null, null,created4),
                createMockEventDetailObject("dc654f9d-1138-485e-9d8f-1caf6eed4591","Target_Object4", null, null,created4),
                createMockEventDetailService("f37f308f-5649-4f85-a9c8-e89c850a2e2a","Target_Service4", null, null,created4),
                createMockEventDetailContext("1e17af47-2875-4766-bb77-73e340ded8b8","Target_Context4", null, null,created4),
                created4
        );
        EventItem cause4 = createMockEventItem(
                "a0e59e4a-1310-47a6-bda2-5d200c4378ea",
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
        event4.setValue(3.0);
        event4.setUnit("Mock_Unit");

        mockEvents.add(event4);

        return mockEvents;
    }

    private EventDetail createMockEventDetail(String id,String name, EventDetail parent, EventDetail child, ZonedDateTime created) {
        EventDetail detail = new EventDetail();

        detail.setId(id);
        detail.setName(name);
        detail.setCreated(created);
        detail.setParent(parent);
        detail.setChild(child);

        return detail;

    }

    private EventItem createMockEventItem(String id,String type, EventDetail subject, EventDetail object, EventDetail service, EventDetail context, ZonedDateTime created)
    {
        EventItem item = new EventItem();
        item.setId(id);
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
}
