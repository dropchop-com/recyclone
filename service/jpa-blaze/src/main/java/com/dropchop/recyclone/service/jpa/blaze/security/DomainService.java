package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.model.entity.jpa.security.EDomain;
import com.dropchop.recyclone.repo.jpa.blaze.security.DomainRepository;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.Implementation;
import com.dropchop.recyclone.service.jpa.blaze.CrudServiceImpl;
import com.dropchop.recyclone.service.jpa.blaze.ServiceConfiguration;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@Implementation(JPA_DEFAULT)
public class DomainService extends CrudServiceImpl<Domain, CodeParams, EDomain, String>
  implements com.dropchop.recyclone.service.api.security.DomainService {

  @Inject
  DomainRepository repository;

  @Inject
  DomainToDtoMapper toDtoMapper;

  @Inject
  DomainToEntityMapper toEntityMapper;

  @Override
  public ServiceConfiguration<Domain, CodeParams, EDomain, String> getConfiguration(CommonExecContext<CodeParams, Domain> execContext) {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper,
      execContext
    );
  }
}
