package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.base.jpa.mapper.security.RoleToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.RoleToJpaMapper;
import com.dropchop.recyclone.base.dto.model.security.Role;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRole;
import com.dropchop.recyclone.base.jpa.repo.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
@SuppressWarnings("unused")
public class RoleMapperProvider extends RecycloneMapperProvider<Role, JpaRole, String> {

  @Inject
  RoleRepository repository;

  @Inject
  RoleToDtoMapper toDtoMapper;

  @Inject
  RoleToJpaMapper toEntityMapper;
}
