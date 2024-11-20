package com.dropchop.recyclone.rest.server.security.intern;

import com.dropchop.recyclone.model.api.rest.ResultCode;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.rest.ResultStatus;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.rest.server.ClassicRestResource;
import com.dropchop.recyclone.service.api.security.SecurityLoadingService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 20. 11. 24.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class SecurityPermissionResource implements
    com.dropchop.recyclone.rest.api.internal.security.SecurityPermissionsResource, ClassicRestResource<RoleNodeParams> {


  @Inject
  SecurityLoadingService securityLoadingService;

  @Inject
  RoleNodeParams params;


  @Override
  public Result<RoleNodePermission> list(RoleNodeParams params) {
    List<RoleNodePermission> roleNodePermissions = this.securityLoadingService.loadRoleNodePermissions(params);
    ResultStatus status = new ResultStatus();
    status.setCode(ResultCode.success);
    status.setTotal(roleNodePermissions.size());
    Result<RoleNodePermission> result = new Result<>();
    result.setData(roleNodePermissions);
    result.setStatus(status);
    return result;
  }
}
