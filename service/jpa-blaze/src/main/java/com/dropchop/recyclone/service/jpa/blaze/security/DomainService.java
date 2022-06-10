package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.model.entity.jpa.security.EDomain;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.security.DomainRepository;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.jpa.blaze.CrudServiceImpl;
import com.dropchop.recyclone.service.jpa.blaze.ServiceConfiguration;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@ServiceType(RCYN_DEFAULT)
public class DomainService extends CrudServiceImpl<Domain, CodeParams, EDomain, String>
  implements com.dropchop.recyclone.service.api.security.DomainService {

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  DomainRepository repository;

  @Inject
  DomainToDtoMapper toDtoMapper;

  @Inject
  DomainToEntityMapper toEntityMapper;

  @Override
  public ServiceConfiguration<Domain, CodeParams, EDomain, String> getConfiguration() {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper
    );
  }
}
