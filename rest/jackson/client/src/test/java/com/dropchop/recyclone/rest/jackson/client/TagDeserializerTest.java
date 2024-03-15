package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.quarkus.runtime.spi.JsonSerializationTypeConfigImpl;
import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 02. 22.
 */
class TagDeserializerTest {
  @SuppressWarnings({"SameParameterValue", "unused"})
  static class CollectionsTypeFactory {
    static JavaType listOf(Class<?> clazz) {
      return TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
    }

    static <T> TypeReference<List<?>> erasedListOf(Class<T> ignored) {
      return new TypeReference<>(){};
    }
  }

  @Test
  void deserialize() throws Exception {
    String json = """
      [
          {
              "title": "Ex Yugoslavic",
              "lang": "en",
              "type": "LanguageGroup",
              "name": "ex_yu"
          },
          {
              "title": "Slavic",
              "lang": "en",
              "type": "LanguageGroup",
              "name": "slavic"
          }
      ]""";

    com.dropchop.recyclone.rest.jackson.client.ObjectMapperFactory mapperFactory = new ObjectMapperFactory(
        new JsonSerializationTypeConfigImpl().addSubType("LanguageGroup", LanguageGroup.class)
    );

    ObjectMapper mapper = mapperFactory.createObjectMapper();
    List<LanguageGroup> groups = mapper.readValue(json, CollectionsTypeFactory.listOf(LanguageGroup.class));
    assertEquals(2, groups.size());
  }
}