package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.jpa.security.RoleToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.RoleToJpaMapper;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRole;
import com.dropchop.recyclone.repo.api.CrudServiceRepository;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
public class RoleRepository extends BlazeRepository<JpaRole, String>
    implements CrudServiceRepository<Role, JpaRole, String> {

  Class<JpaRole> rootClass = JpaRole.class;

  @Inject
  RoleToDtoMapper toDtoMapper;

  @Inject
  RoleToJpaMapper toEntityMapper;
}
