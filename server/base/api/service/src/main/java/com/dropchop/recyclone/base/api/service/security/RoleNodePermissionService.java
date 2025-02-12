package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.api.service.CrudService;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
public interface RoleNodePermissionService extends CrudService<RoleNodePermission> {

  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Security.ROLE;
  }

}
