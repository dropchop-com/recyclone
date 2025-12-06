package com.dropchop.recyclone.base.jpa.service.security;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNodePermission;
import com.dropchop.recyclone.base.jpa.repo.security.RoleNodePermissionMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.security.RoleNodePermissionRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@RequestScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class RoleNodePermissionService extends CrudServiceImpl<RoleNodePermission, JpaRoleNodePermission, UUID>
  implements com.dropchop.recyclone.base.api.service.security.RoleNodePermissionService {

  @Inject
  RoleNodePermissionMapperProvider mapperProvider;

  @Inject
  RoleNodePermissionRepository repository;

  @Inject
  CommonExecContext<RoleNodePermission, ?> executionContext;
}
