package com.dropchop.recyclone.rest.jaxrs.serialization;

import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.dropchop.recyclone.service.api.mapping.DefaultPolymorphicRegistry;
import com.dropchop.recyclone.service.api.mapping.PolymorphicRegistry.SerializationConfig;
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

    ObjectMapperProducer producer = new ObjectMapperProducer();
    producer.polymorphicRegistry = new DefaultPolymorphicRegistry()
      .registerSerializationConfig(
        new SerializationConfig()
          .addSubType("LanguageGroup", LanguageGroup.class)
      );
    ObjectMapper mapper = producer.createObjectMapper();
    List<LanguageGroup> groups = mapper.readValue(json, CollectionsTypeFactory.listOf(LanguageGroup.class));
    assertEquals(2, groups.size());
  }
}