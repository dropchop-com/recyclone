package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.security.EPermission;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.security.PermissionRepository;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.jpa.RecycloneCrudServiceImpl;
import com.dropchop.recyclone.service.api.ServiceConfiguration;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@ServiceType(RECYCLONE_JPA_DEFAULT)
public class PermissionService extends RecycloneCrudServiceImpl<Permission, EPermission, UUID>
  implements com.dropchop.recyclone.service.api.security.PermissionService {

  @Inject
  @RepositoryType(RECYCLONE_JPA_DEFAULT)
  PermissionRepository repository;

  @Inject
  PermissionToDtoMapper toDtoMapper;

  @Inject
  PermissionToEntityMapper toEntityMapper;

  @Override
  public ServiceConfiguration<Permission, EPermission, UUID> getConfiguration() {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper
    );
  }
}
