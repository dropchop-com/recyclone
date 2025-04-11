package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.aggregation.AggregationList;
import com.dropchop.recyclone.base.jackson.*;
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

import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;

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

  public <M extends ObjectMapper> M createObjectMapper(Class<M> mClass) {
    M mapper;
    try {
      mapper = mClass.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.disable(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL);

    SimpleModule module;
    if (this.serializerModifier != null) {
      if (serializerModifier instanceof ExecContextPropertyFilterSerializerModifier) {
        if (mapper instanceof FilteringObjectMapper) {
          module = new SimpleModule();
          module.setSerializerModifier(this.serializerModifier);
          mapper.registerModule(module);
        }
      } else {
        module = new SimpleModule();
        module.setSerializerModifier(this.serializerModifier);
        mapper.registerModule(module);
      }
    }

    if (this.deserializerModifier != null) {
      if (this.deserializerModifier instanceof ParamsFactoryDeserializerModifier) {
        if (mapper instanceof FilteringObjectMapper) {
          module = new SimpleModule();
          module.setDeserializerModifier(this.deserializerModifier);
          mapper.registerModule(module);
        }
      } else {
        module = new SimpleModule();
        module.setDeserializerModifier(this.deserializerModifier);
        mapper.registerModule(module);
      }
    }

    module = new SimpleModule();
    module.addDeserializer(Attribute.class, new AttributeDeserializer());
    module.addDeserializer(Condition.class, new ConditionDeserializer());
    module.addDeserializer(AggregationList.class, new AggregationDeserializer());
    module.addSerializer(new AttributeCompactSerializer());

    mapper.registerModule(module);
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.registerModule(new ParameterNamesModule());

    SimpleModule module1 = new SimpleModule();
    module1.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
    mapper.registerModule(module1);
    mapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false);

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

  public ObjectMapper createFilteringObjectMapper() {
    return createObjectMapper(FilteringObjectMapper.class);
  }

  public ObjectMapper createNonFilteringObjectMapper() {
    return createObjectMapper(DefaultObjectMapper.class);
  }
}
