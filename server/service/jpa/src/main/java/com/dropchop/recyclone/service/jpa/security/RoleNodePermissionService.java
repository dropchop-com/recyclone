package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.api.invoke.CommonExecContextContainer;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermission;
import com.dropchop.recyclone.repo.api.FilteringMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodeMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodePermissionMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodePermissionRepository;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodeRepository;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
public class RoleNodePermissionService extends CrudServiceImpl<RoleNodePermission, JpaRoleNodePermission, UUID>
  implements com.dropchop.recyclone.service.api.security.RoleNodePermissionService {

  @Inject
  RoleNodePermissionMapperProvider mapperProvider;

  @Inject
  RoleNodePermissionRepository repository;

  @Inject
  CommonExecContextContainer execContextContainer;

  @Override
  public RoleNodePermissionRepository getRepository() {
    return repository;
  }

  @Override
  public FilteringMapperProvider<RoleNodePermission, JpaRoleNodePermission, UUID> getMapperProvider() {
    return mapperProvider;
  }

  @Transactional
  public Result<RoleNode> addPermissions(RoleNodeParams params) {
    Result<RoleNode> result = new Result<>();
    return result;
  }

  @Transactional
  public Result<RoleNode> removePermissions(RoleNodeParams params) {
    Result<RoleNode> result = new Result<>();
    return result;
  }


}
