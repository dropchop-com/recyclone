package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.jpa.security.PermissionToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.PermissionToJpaMapper;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaDomain;
import com.dropchop.recyclone.model.entity.jpa.security.JpaPermission;
import com.dropchop.recyclone.repo.api.CrudServiceRepository;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
@RepositoryType(RECYCLONE_JPA_DEFAULT)
public class PermissionRepository extends BlazeRepository<JpaPermission, UUID>
    implements CrudServiceRepository<Permission, JpaPermission, UUID> {

  Class<JpaPermission> rootClass = JpaPermission.class;

  @Inject
  PermissionToDtoMapper toDtoMapper;

  @Inject
  PermissionToJpaMapper toEntityMapper;
}
