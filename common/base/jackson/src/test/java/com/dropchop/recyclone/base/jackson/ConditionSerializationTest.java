package com.dropchop.recyclone.base.jackson;

import com.dropchop.recyclone.base.api.model.query.condition.And;
import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static com.dropchop.recyclone.base.api.model.query.Condition.*;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.*;
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
        field("created", lte(Iso8601.fromIso("2024-09-19T10:12:01.123"))),
        field("created", gte(Iso8601.fromIso("2024-09-19T10:12:01.123"))),
        field("created", lt(Iso8601.fromIso("2024-09-19T10:12:01.123"))),
        field("created", gt(Iso8601.fromIso("2024-09-19T10:12:01.123"))),
        field("created", eq(Iso8601.fromIso("2024-09-19T10:12:01.123"))),
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

  @Test
  void testPhraseProcessing() throws Exception {
    Condition c = and(
        phrase("text", "krem proti gubam")
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

  @Test
  void testWildcardProcessing() throws Exception {
    Condition c = and(
        wildcard("text", "krem*")
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

  @Test
  void testAdvancedTextProcessing() throws Exception {
    Condition c = and(
      advancedText("text", "\"krem* proti gubam\"")
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

  @Test
  void testSimpleKnnSerialization() throws Exception {
    Condition c = and(
      knn("product_embedding", new float[]{0.1f, 0.2f, 0.3f, 0.4f, 0.5f}, 3)
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

  @Test
  void testKnnWithKSerialization() throws Exception {
    Condition c = and(
      knn("user_behavior_embedding", new float[]{0.3f, 0.7f, 0.9f}, 20)
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

  @Test
  void testKnnWithFilterSerialization() throws Exception {
    Condition c = and(
      knn("article_embedding",
        new float[]{0.2f, 0.4f, 0.6f, 0.8f},
        12,
        or(
          and(
            field("status", eq("published")),
            field("views", gte(1000))
          ),
          field("featured", eq(true))
        )
      )
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

  @Test
  void testKnnWithSimilaritySerialization() throws Exception {
    Condition c = and(
      knn("document_embedding",
        new float[]{1.0f, 0.5f, -0.3f},
        5,
        null,
        0.8f)
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

  @Test
  void testKnnWithFilterAndSimilaritySerialization() throws Exception {
    Condition c = and(
      knn("product_features",
        new float[]{0.4f, 0.6f, 0.2f, 0.8f},
        10,
        and(
          field("category", in("electronics", "gadgets")),
          field("price", gteLt(50.0, 1000.0)),
          not(field("discontinued", eq(true)))
        ),
        0.7f
      )
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

  @Test
  void testMultipleKnnSerialization() throws Exception {
    Condition c = and(
      or(
        knn("text_embedding", new float[]{0.1f, 0.2f}, 5),
        knn("image_embedding", new float[]{0.3f, 0.4f}, 3)
      ),
      field("status", eq("active"))
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

  @Test
  void testKnnWithComplexFilterSerialization() throws Exception {
    Condition c = and(
      knn("semantic_embedding",
        new float[]{-0.1f, 0.25f, 0.8f, -0.3f, 0.9f},
        15,
        or(
          and(
            field("type", eq("article")),
            field("published_date", gte(Iso8601.fromIso("2024-01-01T00:00:00.000")))
          ),
          and(
            field("type", eq("blog")),
            field("author", in("john", "jane", "bob"))
          )
        ),
        0.75f
      ),
      field("language", eq("en"))
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
