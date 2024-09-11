package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.jpa.security.RoleToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.RoleToJpaMapper;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRole;
import com.dropchop.recyclone.repo.jpa.blaze.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
public class RoleMapperProvider extends RecycloneMapperProvider<Role, JpaRole, String> {

  @Inject
  RoleRepository repository;

  @Inject
  RoleToDtoMapper toDtoMapper;

  @Inject
  RoleToJpaMapper toEntityMapper;
}
