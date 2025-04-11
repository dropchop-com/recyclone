package com.dropchop.recyclone.quarkus.it.repo.es;

import com.dropchop.recyclone.base.api.repo.FilteringMapperProvider;
import com.dropchop.recyclone.quarkus.it.mapper.es.DummyToDtoMapper;
import com.dropchop.recyclone.quarkus.it.mapper.es.DummyToEsMapper;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.model.entity.es.EsDummy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

@Getter
@ApplicationScoped
public class ElasticDummyMapperProvider extends FilteringMapperProvider<Dummy, EsDummy, String> {

  @Inject
  ElasticDummyRepository repository;

  @Inject
  DummyToDtoMapper toDtoMapper;

  @Inject
  DummyToEsMapper toEntityMapper;

}
