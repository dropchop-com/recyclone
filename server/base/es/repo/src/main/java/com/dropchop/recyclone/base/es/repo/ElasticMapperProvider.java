package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.mapper.MapToEntityMapper;

public interface ElasticMapperProvider<E> {
   MapToEntityMapper<E> getMapToEntityMapper();
}
