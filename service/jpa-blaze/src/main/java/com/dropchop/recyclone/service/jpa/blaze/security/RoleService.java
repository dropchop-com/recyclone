package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.entity.jpa.security.ERole;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleRepository;
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
public class RoleService extends CrudServiceImpl<Role, CodeParams, ERole, String>
  implements com.dropchop.recyclone.service.api.security.RoleService {

  @Inject
  RoleRepository repository;

  @Inject
  RoleToDtoMapper toDtoMapper;

  @Inject
  RoleToEntityMapper toEntityMapper;

  @Override
  public ServiceConfiguration<Role, CodeParams, ERole, String> getConfiguration(CommonExecContext<CodeParams, Role> execContext) {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper,
      execContext
    );
  }
}
