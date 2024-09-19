package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.model.api.utils.Iso8601;
import com.dropchop.recyclone.model.api.query.And;
import com.dropchop.recyclone.model.api.query.Condition;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static com.dropchop.recyclone.model.api.query.Condition.*;
import static com.dropchop.recyclone.model.api.query.Condition.field;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.*;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@Slf4j
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
                field(
                    "neki", in("one", "two", "three")
                )
            ),
            field(
                "modified", Iso8601.fromIso("2024-09-19T10:12:01.123")
            ),
            not(
                field(
                "uuid", in(
                    "6ad7cbc2-fdc3-4eb3-bb64-ba6a510004db", "c456c510-3939-4e2a-98d1-3d02c5d2c609"
                    )
                )
            )
        ),
        field(
            "type", in(1, 2, 3)
        ),
        field(
            "created", Iso8601.fromIso("2024-09-19T10:12:01.123")
        )
    ).and(
        field(
            "type2", in(1, 2, 3)
        )
    );
    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();
    String jsonOutput = mapper.writeValueAsString(c);
    log.info("[{}]", jsonOutput);
    Condition x = mapper.readValue(jsonOutput, And.class);
    log.info("[{}]", x);
  }
}
