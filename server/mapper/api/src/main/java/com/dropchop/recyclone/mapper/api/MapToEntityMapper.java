package com.dropchop.recyclone.mapper.api;

import java.util.Map;

public interface MapToEntityMapper<E> {
  E fromMap(Map<String, Object> result);

}
