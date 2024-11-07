package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.service.api.CrudService;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
public interface RoleNodeService extends CrudService<RoleNode> {

  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Security.ROLE;
  }


  Result<RoleNode> addPermissions(RoleNodeParams params);
  Result<RoleNode> removePermissions(RoleNodeParams params);
}
