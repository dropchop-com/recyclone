package com.dropchop.recyclone.base.api.repo;

import com.dropchop.recyclone.base.api.mapper.MapToEntityMapper;

@SuppressWarnings("unused")
public interface ElasticMapperProvider {
   MapToEntityMapper<?> getMapToEntityMapper();
}
