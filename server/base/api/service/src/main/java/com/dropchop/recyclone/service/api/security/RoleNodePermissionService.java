package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.service.api.CrudService;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
public interface RoleNodePermissionService extends CrudService<RoleNodePermission> {

  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Security.ROLE;
  }

}
