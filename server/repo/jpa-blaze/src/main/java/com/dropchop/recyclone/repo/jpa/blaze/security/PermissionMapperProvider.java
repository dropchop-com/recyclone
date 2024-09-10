package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.jpa.security.PermissionToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.PermissionToJpaMapper;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaPermission;
import com.dropchop.recyclone.repo.api.MapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
public class PermissionMapperProvider implements MapperProvider<Permission, JpaPermission> {
  @Inject
  PermissionToDtoMapper toDtoMapper;

  @Inject
  PermissionToJpaMapper toEntityMapper;
}
