package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.jpa.security.RoleNodeToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.RoleNodeToJpaMapper;
import com.dropchop.recyclone.mapper.jpa.security.RoleToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.RoleToJpaMapper;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRole;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNode;
import com.dropchop.recyclone.repo.jpa.blaze.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@ApplicationScoped
public class RoleNodeMapperProvider extends RecycloneMapperProvider<RoleNode, JpaRoleNode, String> {

  @Inject
  RoleNodeRepository repository;

  @Inject
  RoleNodeToDtoMapper toDtoMapper;

  @Inject
  RoleNodeToJpaMapper toEntityMapper;
}
