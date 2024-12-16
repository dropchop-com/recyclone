package com.dropchop.recyclone.repo.es;

import com.dropchop.recyclone.mapper.api.MapToEntityMapper;

public interface ElasticMapperProvider {
   MapToEntityMapper getMapToEntityMapper();
}
