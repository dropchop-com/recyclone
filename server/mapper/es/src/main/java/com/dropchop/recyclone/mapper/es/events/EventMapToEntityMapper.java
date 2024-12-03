package com.dropchop.recyclone.mapper.es.events;

import com.dropchop.recyclone.mapper.api.MapToEntityMapper;
import com.dropchop.recyclone.model.entity.es.event.EsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;


/**
 * @author Samo Pritrznik <armando.ota@dropchop.com> on 03. 12. 24
 **/
@SuppressWarnings("unused")
@Mapper(componentModel = "jakarta-cdi")
public interface EventMapToEntityMapper extends MapToEntityMapper<EsEvent> {

  EventMapToEntityMapper INSTANCE = Mappers.getMapper(EventMapToEntityMapper.class);

  EsEvent fromMap(Map<String, Object> event);

  default String mapToString(Object value) {
    return value != null ? value.toString() : null;
  }

  default UUID mapToUUID(Object value) {
    if (value instanceof String) {
      return UUID.fromString((String) value);
    }
    return null;
  }

  default Boolean mapToBoolean(Object value) {
    if (value instanceof Boolean) {
      return (Boolean) value;
    }
    if (value instanceof String) {
      return Boolean.parseBoolean((String) value);
    }
    return null;
  }

  default Integer mapToInteger(Object value) {
    if (value instanceof Integer) {
      return (Integer) value;
    }
    if (value instanceof String) {
      return Integer.parseInt((String) value);
    }
    return null;
  }

  default Double mapToFloat(Object value) {
    if (value instanceof Float) {
      return (Double) value;
    }
    if (value instanceof String) {
      return Double.parseDouble((String) value);
    }
    return null;
  }

  default ZonedDateTime mapToZonedDateTime(Object value) {
    if (value instanceof String) {
      return ZonedDateTime.parse((String) value);
    }
    return null;
  }
}
