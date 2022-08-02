package com.dropchop.recyclone.rest.jaxrs.serialization;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.service.api.mapping.PolymorphicRegistry;
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
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 08. 22.
 */
@Slf4j
public class ObjectMapperFactory {

  public ObjectMapper decorate(ObjectMapper mapper, PolymorphicRegistry polymorphicRegistry) {
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.disable(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL);

    SimpleModule module = new SimpleModule();
    module.addDeserializer(Attribute.class, new AttributeDeserializer());
    module.addSerializer(new AttributeCompactSerializer());

    mapper.registerModule(module);
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.registerModule(new ParameterNamesModule());
    if (polymorphicRegistry != null) {
      for (PolymorphicRegistry.SerializationConfig serializationConfig : polymorphicRegistry.getSerializationConfigs()) {
        serializationConfig.getSubTypeMap().forEach(
          (key, value) -> mapper.registerSubtypes(new NamedType(value, key))
        );
        serializationConfig.getMixIns().forEach(mapper::addMixIn);
      }
    } else {
      log.warn("Missing polymorphic registry while creating JSON mapper!");
    }
    return mapper;
  }

  public ObjectMapper createObjectMapper(PolymorphicRegistry polymorphicRegistry) {
    ObjectMapper mapper = new ObjectMapper();
    decorate(mapper, polymorphicRegistry);
    return mapper;
  }
}
