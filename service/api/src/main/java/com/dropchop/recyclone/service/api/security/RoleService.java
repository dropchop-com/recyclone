package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.service.api.CrudService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 12. 21.
 */
public interface RoleService extends CrudService<Role, RoleParams> {

  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Security.ROLE;
  }

  Result<Role> addPermissions(RoleParams params);
  Result<Role> removePermissions(RoleParams params);
}
