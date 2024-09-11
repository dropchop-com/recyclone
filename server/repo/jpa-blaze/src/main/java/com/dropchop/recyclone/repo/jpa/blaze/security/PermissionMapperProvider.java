package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.jpa.security.PermissionToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.PermissionToJpaMapper;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaPermission;
import com.dropchop.recyclone.repo.jpa.blaze.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
public class PermissionMapperProvider extends RecycloneMapperProvider<Permission, JpaPermission, UUID> {

  @Inject
  PermissionRepository repository;

  @Inject
  PermissionToDtoMapper toDtoMapper;

  @Inject
  PermissionToJpaMapper toEntityMapper;
}
