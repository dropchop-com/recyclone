package com.dropchop.recyclone.quarkus.it.service.alt;

import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaDummy;
import com.dropchop.recyclone.quarkus.it.repo.jpa.DummyRepository;
import com.dropchop.recyclone.quarkus.it.service.jpa.blaze.DummyToDtoMapper;
import com.dropchop.recyclone.quarkus.it.service.jpa.blaze.DummyToEntityMapper;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.service.api.ServiceConfiguration;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.jpa.RecycloneCrudServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@ServiceType("alter")
public class DummyService extends RecycloneCrudServiceImpl<Dummy, JpaDummy, String>
  implements com.dropchop.recyclone.quarkus.it.service.api.DummyService {

  @Inject
  @RepositoryType(RECYCLONE_JPA_DEFAULT)
  DummyRepository repository;

  @Inject
  DummyToDtoMapper toDtoMapper;

  @Inject
  DummyToEntityMapper toEntityMapper;

  @Override
  public ServiceConfiguration<Dummy, JpaDummy, String> getConfiguration() {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper
    );
  }
}
