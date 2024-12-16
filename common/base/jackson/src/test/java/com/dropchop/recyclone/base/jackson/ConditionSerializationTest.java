package com.dropchop.recyclone.base.jackson;

import com.dropchop.recyclone.base.api.model.query.condition.And;
import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static com.dropchop.recyclone.base.api.model.query.Condition.*;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.gteLt;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.in;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
public class ConditionSerializationTest {
  @Test
  void testComposition() throws Exception {
    Condition c = and(
        or(
            field(
                "updated",
                gteLt(
                    Iso8601.fromIso("2024-09-19T10:12:01.123"),
                    Iso8601.fromIso("2024-09-20T11:00:01.123")
                )
            ),
            and(
                field("neki", in("one", "two", "three"))
            ),
            field("modified", Iso8601.fromIso("2024-09-19T10:12:01.123")),
            not(
                field(
                "uuid", in("6ad7cbc2-fdc3-4eb3-bb64-ba6a510004db", "c456c510-3939-4e2a-98d1-3d02c5d2c609")
                )
            )
        ),
        field("type", in(1, 2, 3)),
        field("created", Iso8601.fromIso("2024-09-19T10:12:01.123")),
        field("miki", null)
    ).and(
        field("type2", in(1, 2, 3))
    );
    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();
    // write original
    String jsonOutput1 = mapper.writeValueAsString(c);
    // parse original to duplicate
    Condition x = mapper.readValue(jsonOutput1, And.class);
    // write duplicate
    String jsonOutput2 = mapper.writeValueAsString(x);
    // compare
    assertEquals(jsonOutput1, jsonOutput2);
  }
}
