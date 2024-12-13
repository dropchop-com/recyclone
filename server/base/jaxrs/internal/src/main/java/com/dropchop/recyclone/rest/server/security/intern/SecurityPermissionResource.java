package com.dropchop.recyclone.rest.server.security.intern;

import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.rest.ResultStatus;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.rest.server.ClassicRestResource;
import com.dropchop.recyclone.service.api.security.SecurityLoadingService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 20. 11. 24.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class SecurityPermissionResource implements
    com.dropchop.recyclone.rest.api.internal.security.SecurityPermissionsResource, ClassicRestResource<RoleNodePermission> {


  @Inject
  SecurityLoadingService securityLoadingService;

  @Override
  public Result<RoleNodePermission> list(RoleNodeParams params) {
    if (params.getFilter().getContent().getTreeLevel() == null) {
      params.getFilter().getContent().setTreeLevel(3); //no need to deserialize whole role node on permission
    }
    Collection<RoleNodePermission> roleNodePermissions = this.securityLoadingService.loadRoleNodePermissions(params);
    ResultStatus status = new ResultStatus();
    status.setCode(ResultCode.success);
    status.setTotal(roleNodePermissions.size());
    List<RoleNodePermission> permissions = new ArrayList<>(roleNodePermissions);
    Result<RoleNodePermission> result = new Result<>();
    result.setData(permissions);
    result.setStatus(status);
    return result;
  }
}
