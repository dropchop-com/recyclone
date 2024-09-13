package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 23. 06. 22.
 */
@Slf4j
public class ObjectMapperFactory {

  private final JsonSerializationTypeConfig serializationTypeConfig;

  public ObjectMapperFactory(JsonSerializationTypeConfig polymorphicRegistry) {
   this.serializationTypeConfig = polymorphicRegistry;
  }

  public ObjectMapperFactory() {
    this(null);
  }

  public ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    SimpleModule module = new SimpleModule();
    module.addDeserializer(Attribute.class, new AttributeDeserializer());
    module.addSerializer(new AttributeCompactSerializer());

    mapper.registerModule(module);
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.registerModule(new ParameterNamesModule());
    if (serializationTypeConfig != null) {

      serializationTypeConfig.getSubTypeMap().forEach(
        (key, value) -> {
          log.info("Registering named type value [{}] for key [{}].", value, key);
          mapper.registerSubtypes(new NamedType(value, key));
        }
      );
      serializationTypeConfig.getMixIns().forEach(mapper::addMixIn);

    } else {
      log.debug("Missing polymorphic registry while creating JSON mapper!");
    }
    return mapper;
  }
}
