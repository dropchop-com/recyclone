package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.model.entity.jpa.security.EDomain;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.security.DomainRepository;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.jpa.blaze.RecycloneCrudServiceImpl;
import com.dropchop.recyclone.service.api.ServiceConfiguration;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@ServiceType(RCYN_DEFAULT)
public class DomainService extends RecycloneCrudServiceImpl<Domain, EDomain, String>
  implements com.dropchop.recyclone.service.api.security.DomainService {

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  DomainRepository repository;

  @Inject
  DomainToDtoMapper toDtoMapper;

  @Inject
  DomainToEntityMapper toEntityMapper;

  @Override
  public ServiceConfiguration<Domain, EDomain, String> getConfiguration() {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper
    );
  }
}
