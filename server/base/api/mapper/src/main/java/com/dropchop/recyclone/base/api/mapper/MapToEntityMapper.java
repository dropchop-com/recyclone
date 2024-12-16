package com.dropchop.recyclone.base.api.mapper;

import java.util.Map;

@SuppressWarnings("unused")
public interface MapToEntityMapper<E> {
  E fromMap(Map<String, Object> result);
}
