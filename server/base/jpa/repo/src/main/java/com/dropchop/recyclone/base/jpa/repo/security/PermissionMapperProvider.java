package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.base.jpa.mapper.security.PermissionToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.PermissionToJpaMapper;
import com.dropchop.recyclone.base.dto.model.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaPermission;
import com.dropchop.recyclone.base.jpa.repo.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
@SuppressWarnings("unused")
public class PermissionMapperProvider extends RecycloneMapperProvider<Permission, JpaPermission, UUID> {

  @Inject
  PermissionRepository repository;

  @Inject
  PermissionToDtoMapper toDtoMapper;

  @Inject
  PermissionToJpaMapper toEntityMapper;
}
