package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.rest.jackson.client.AttributeCompactSerializer;
import com.dropchop.recyclone.rest.jackson.client.AttributeDeserializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CDI Capable ObjectMapperFactory extension.
 * Usage: Inject it and wrap createObjectMapper() in Producer method to customize ObjectMapper.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 23. 06. 22.
 * @noinspection unused
 */
@ApplicationScoped
public class ObjectMapperFactory {

  private final Logger log = LoggerFactory.getLogger(ObjectMapperFactory.class);

  private final JsonSerializationTypeConfig serializationTypeConfig;
  private final BeanSerializerModifier serializerModifier;
  private final BeanDeserializerModifier deserializerModifier;

  @Inject
  public ObjectMapperFactory(JsonSerializationTypeConfig polymorphicRegistry,
                             BeanSerializerModifier serializerModifier,
                             BeanDeserializerModifier deserializerModifier) {
    this.serializationTypeConfig = polymorphicRegistry;
    this.serializerModifier = serializerModifier;
    this.deserializerModifier = deserializerModifier;
  }

  public ObjectMapperFactory(BeanSerializerModifier serializerModifier,
                             BeanDeserializerModifier deserializerModifier) {
    this(null, serializerModifier, deserializerModifier);
  }

  public ObjectMapperFactory(BeanSerializerModifier serializerModifier) {
    this(null, serializerModifier, null);
  }

  public ObjectMapperFactory(BeanDeserializerModifier deserializerModifier) {
    this(null, null, deserializerModifier);
  }

  public ObjectMapperFactory() {
    this(null, null, null);
  }

  public ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.disable(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL);

    SimpleModule module;
    if (this.serializerModifier != null) {
      module = new SimpleModule();
      module.setSerializerModifier(this.serializerModifier);
      mapper.registerModule(module);
    }

    if (this.deserializerModifier != null) {
      module = new SimpleModule();
      module.setDeserializerModifier(this.deserializerModifier);
      mapper.registerModule(module);
    }

    module = new SimpleModule();
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
