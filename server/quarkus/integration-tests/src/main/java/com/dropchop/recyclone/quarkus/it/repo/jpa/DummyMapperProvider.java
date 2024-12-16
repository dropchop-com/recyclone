package com.dropchop.recyclone.quarkus.it.repo.jpa;

import com.dropchop.recyclone.quarkus.it.mapper.es.DummyToEsMapper;
import com.dropchop.recyclone.quarkus.it.mapper.jpa.DummyMapToDtoMapper;
import com.dropchop.recyclone.quarkus.it.mapper.jpa.DummyToDtoMapper;
import com.dropchop.recyclone.quarkus.it.mapper.jpa.DummyToJpaMapper;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaDummy;
import com.dropchop.recyclone.quarkus.it.repo.DummyRepository;
import com.dropchop.recyclone.base.api.repo.FilteringMapperProvider;
import com.dropchop.recyclone.base.api.service.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
@SuppressWarnings("unused")
public class DummyMapperProvider extends FilteringMapperProvider<Dummy, JpaDummy, String> {

  @Inject
  @RecycloneType("alter")
  DummyRepository repository;

  @Inject
  DummyToDtoMapper toDtoMapper;

  @Inject
  DummyToJpaMapper toEntityMapper;

  @Inject
  DummyToEsMapper toEsEntityMapper;

  @Inject
  com.dropchop.recyclone.quarkus.it.mapper.es.DummyToDtoMapper toEsDtoMapper;

  @Inject
  DummyMapToDtoMapper mapToDtoMapper;
}
