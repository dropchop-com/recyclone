package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.security.RoleNode;
import com.dropchop.recyclone.base.api.service.CrudService;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
public interface RoleNodeService extends CrudService<RoleNode> {

  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Security.ROLE;
  }

}
