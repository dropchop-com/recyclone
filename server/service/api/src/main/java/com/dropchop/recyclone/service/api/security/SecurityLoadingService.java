package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.service.api.Service;

import java.util.Collection;
import java.util.List;

public interface SecurityLoadingService extends Service {

  default String getSecurityDomain() {
    return Constants.Domains.Security.PERMISSION;
  }

  /**
   * Resolves role node permissions for entity and/or target defined by exactly 1 role node!
   * @param params - parameters with entity and/or target data.
   * @return list of resolved role node permissions
   */
  Collection<RoleNodePermission> loadRoleNodePermissions(RoleNodeParams params);


  /**
   * Resolves Apache Shiro permissions for entity and/or target defined by exactly 1 role node!
   * @param params - parameters with entity and/or target data
   * @return list of resolved apache shiro permissions
   */
  Collection<Permission> loadPermissions(RoleNodeParams params);


}
