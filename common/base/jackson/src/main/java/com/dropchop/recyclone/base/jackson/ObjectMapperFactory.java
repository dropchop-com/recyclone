package com.dropchop.recyclone.base.jackson;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.aggregation.AggregationList;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;

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
}
