package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.security.EPermission;
import com.dropchop.recyclone.repo.jpa.blaze.security.PermissionRepository;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.Implementation;
import com.dropchop.recyclone.service.jpa.blaze.CrudServiceImpl;
import com.dropchop.recyclone.service.jpa.blaze.ServiceConfiguration;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.JPA_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@Implementation(JPA_DEFAULT)
public class PermissionService extends CrudServiceImpl<Permission, CodeParams, EPermission, UUID>
  implements com.dropchop.recyclone.service.api.security.PermissionService {

  @Inject
  PermissionRepository repository;

  @Inject
  PermissionToDtoMapper toDtoMapper;

  @Inject
  PermissionToEntityMapper toEntityMapper;

  @Override
  public ServiceConfiguration<Permission, CodeParams, EPermission, UUID> getConfiguration(CommonExecContext<CodeParams, Permission> execContext) {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper,
      execContext
    );
  }
}
