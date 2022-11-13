package com.dropchop.recyclone.rest.jackson.server;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistry;
import com.dropchop.recyclone.rest.jackson.client.AttributeCompactSerializer;
import com.dropchop.recyclone.rest.jackson.client.AttributeDeserializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 23. 06. 22.
 */
@Slf4j
public class ObjectMapperFactory {

  private final PolymorphicRegistry polymorphicRegistry;
  private final BeanSerializerModifier serializerModifier;

  public ObjectMapperFactory(PolymorphicRegistry polymorphicRegistry,
                             BeanSerializerModifier serializerModifier) {
    this.polymorphicRegistry = polymorphicRegistry;
    this.serializerModifier = serializerModifier;
  }

  public ObjectMapperFactory(PolymorphicRegistry polymorphicRegistry) {
    this(polymorphicRegistry, null);
  }

  public ObjectMapperFactory(BeanSerializerModifier serializerModifier) {
    this(null, serializerModifier);
  }

  public ObjectMapperFactory() {
    this(null, null);
  }

  public ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.disable(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL);

    SimpleModule module;
    if (this.serializerModifier != null) {
      module = new SimpleModule();
      module.setSerializerModifier(serializerModifier);
      mapper.registerModule(module);
    }

    module = new SimpleModule();
    module.addDeserializer(Attribute.class, new AttributeDeserializer());
    module.addSerializer(new AttributeCompactSerializer());

    mapper.registerModule(module);
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.registerModule(new ParameterNamesModule());
    if (polymorphicRegistry != null) {
      for (PolymorphicRegistry.SerializationConfig serializationConfig : polymorphicRegistry.getSerializationConfigs()) {
        serializationConfig.getSubTypeMap().forEach(
          (key, value) -> {
            log.info("Registering named type value [{}] for key [{}].", value, key);
            mapper.registerSubtypes(new NamedType(value, key));
          }
        );
        serializationConfig.getMixIns().forEach(mapper::addMixIn);
      }
    } else {
      log.debug("Missing polymorphic registry while creating JSON mapper!");
    }
    return mapper;
  }
}
