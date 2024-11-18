package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermission;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodePermissionMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodePermissionRepository;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
public class RoleNodePermissionService extends CrudServiceImpl<RoleNodePermission, JpaRoleNodePermission, UUID>
  implements com.dropchop.recyclone.service.api.security.RoleNodePermissionService {

  @Inject
  RoleNodePermissionMapperProvider mapperProvider;

  @Inject
  RoleNodePermissionRepository repository;

}
