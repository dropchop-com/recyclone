package com.dropchop.recyclone.base.dto.model.invoke;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.Field;
import com.dropchop.recyclone.base.api.model.query.condition.And;
import org.junit.jupiter.api.Test;

import static com.dropchop.recyclone.base.api.model.query.Condition.and;
import static com.dropchop.recyclone.base.api.model.query.Condition.field;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 11. 2025.
 */
class QueryParamsTest {

  @Test
  void testBuilder() {
    QueryParams.QueryParamsBuilder<?, ?> builder = QueryParams
        .builder()
        .condition(
            and(field("test0", 1))
        ).and(field("test1", 1));

    Condition condition = builder.build().getCondition();
    if (!(condition instanceof And cnd)) {
      throw new AssertionError("Condition is not an And!");
    }
    assertEquals(2, cnd.get$and().size());
    assertEquals("test0", ((Field<?>)cnd.get$and().getFirst()).getName());
    assertEquals("test1", ((Field<?>)cnd.get$and().getLast()).getName());
  }
}