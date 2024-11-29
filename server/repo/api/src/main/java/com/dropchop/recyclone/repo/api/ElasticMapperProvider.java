package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.mapper.api.MapToEntityMapper;

public interface ElasticMapperProvider {
   MapToEntityMapper getMapToEntityMapper();
}
