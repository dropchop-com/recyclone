package com.dropchop.recyclone.model.dto.query;

import com.dropchop.recyclone.model.api.query.Condition;
import com.dropchop.recyclone.model.api.utils.Iso8601;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static com.dropchop.recyclone.model.api.query.Condition.*;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.eq;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.gteLt;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@Slf4j
class ConditionTest {

  @Test
  void testComposition() {
    Condition c = and(
        or(
            field(
                "updated",
                gteLt(
                    Iso8601.fromIso("2024-09-19T10:12:01.123"),
                    Iso8601.fromIso("2024-09-20T11:00:01.123")
                )
            ),
            field(
                "modified",
                eq(Iso8601.fromIso("2024-09-19T10:12:01.123"))
            )
        ),
        field(
            "created",
            eq(Iso8601.fromIso("2024-09-19T10:12:01.123"))
        )
    );
  }
}