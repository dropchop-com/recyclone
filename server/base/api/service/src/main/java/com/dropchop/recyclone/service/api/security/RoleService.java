package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.invoke.RoleParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.Role;
import com.dropchop.recyclone.service.api.CrudService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
public interface RoleService extends CrudService<Role> {

  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Security.ROLE;
  }

  Result<Role> addPermissions(RoleParams params);
  Result<Role> removePermissions(RoleParams params);
}
