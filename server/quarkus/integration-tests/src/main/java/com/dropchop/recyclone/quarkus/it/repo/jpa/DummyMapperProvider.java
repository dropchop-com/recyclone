package com.dropchop.recyclone.quarkus.it.repo.jpa;

import com.dropchop.recyclone.quarkus.it.mapper.jpa.DummyMapToDtoMapper;
import com.dropchop.recyclone.quarkus.it.mapper.jpa.DummyToDtoMapper;
import com.dropchop.recyclone.quarkus.it.mapper.jpa.DummyToJpaMapper;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaDummy;
import com.dropchop.recyclone.repo.api.FilteringMapperProvider;
import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
public class DummyMapperProvider extends FilteringMapperProvider<Dummy, JpaDummy, String> {

  @Inject
  @RecycloneType("alter")
  com.dropchop.recyclone.quarkus.it.repo.DummyRepository repository;

  @Inject
  DummyToDtoMapper toDtoMapper;

  @Inject
  DummyToJpaMapper toEntityMapper;

  @Inject
  DummyMapToDtoMapper mapToDtoMapper;
}
