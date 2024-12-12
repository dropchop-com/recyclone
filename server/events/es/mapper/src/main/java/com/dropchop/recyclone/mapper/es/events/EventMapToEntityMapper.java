package com.dropchop.recyclone.mapper.es.events;

import com.dropchop.recyclone.mapper.api.MapToEntityMapper;
import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.entity.es.attr.EsAttribute;
import com.dropchop.recyclone.model.entity.es.events.EsEvent;
import com.dropchop.recyclone.model.entity.es.events.EsEventDetail;
import com.dropchop.recyclone.model.entity.es.events.EsEventItem;
import com.dropchop.recyclone.model.entity.es.events.EsEventTrace;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
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

  default EsEventItem mapToEsEventItem(Object value) {
    if (value instanceof Map valueMap) {
      EsEventItem esEventItem = new EsEventItem();
      esEventItem.setUuid(String.valueOf(valueMap.get("uuid")));
      esEventItem.setService(this.mapToEsEventDetail(valueMap.get("service")));
      esEventItem.setObject(this.mapToEsEventDetail(valueMap.get("object")));
      esEventItem.setContext(this.mapToEsEventDetail(valueMap.get("context")));
      esEventItem.setSubject(this.mapToEsEventDetail(valueMap.get("subject")));
      esEventItem.setCreated(this.mapToZonedDateTime(valueMap.get("created")));
      return esEventItem;
    }
    return null;
  }

  default EsEventTrace mapToEsEventTrace(Object value) {
    if (value instanceof Map valueMap) {
      EsEventTrace esEventTrace = new EsEventTrace();
      esEventTrace.setUuid(String.valueOf(valueMap.get("uuid")));
      esEventTrace.setContext(String.valueOf(valueMap.get("context")));
      esEventTrace.setGroup(String.valueOf(valueMap.get("group")));
      return esEventTrace;
    }
    return null;
  }

  default EsEventDetail mapToEsEventDetail(Object value) {
    if (value instanceof Map valueMap) {
      EsEventDetail esEventDetail = new EsEventDetail();
      esEventDetail.setUuid(String.valueOf(valueMap.get("uuid")));
      esEventDetail.setName(String.valueOf(valueMap.get("name")));
      esEventDetail.setParent(this.mapToEsEventDetail(valueMap.get("parent")));
      esEventDetail.setChild(this.mapToEsEventDetail(valueMap.get("child")));
      esEventDetail.setCreated(this.mapToZonedDateTime(valueMap.get("created")));
      return esEventDetail;
    }
    return null;
  }

  default Set<Attribute<?>> mapToAttributes(Object value) {
    if (value instanceof Collection collection) {
      for (Object attributeObj : collection) {
        if (attributeObj instanceof Map attribute) {
          attribute.get("something");
        }
      }
    }
    return null;
  }

  default Set<EsAttribute<?>> mapToEsAttributes(Object value) {
    if (value instanceof Collection collection) {
      for (Object attributeObj : collection) {
        if (attributeObj instanceof Map attribute) {
          attribute.get("something");
        }
      }
    }
    return null;
  }
}
