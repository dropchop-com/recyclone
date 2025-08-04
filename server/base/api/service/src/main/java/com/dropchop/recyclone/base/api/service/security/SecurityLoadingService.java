package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.invoke.RoleNodeParams;
import com.dropchop.recyclone.base.dto.model.security.Permission;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.api.service.Service;

import java.util.Collection;
import java.util.Set;

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
   * Loads a collection of permissions for a specific user and a set of domain prefixes.
   *
   * @param user the user for whom permissions are being loaded
   * @param domainPrefixes the set of domain prefixes to filter permissions by
   * @return a collection of permissions associated with the given user and domain prefixes
   */
  Collection<Permission> loadPermissions(User user, Set<String> domainPrefixes);

  /**
   * Resolves Apache Shiro permissions for entity and/or target defined by exactly 1 role node!
   * @param params - parameters with entity and/or target data
   * @return list of resolved apache shiro permissions
   */
  Collection<Permission> loadPermissions(RoleNodeParams params);

  /**
   * Loads user by login name.
   * @param userId - user login name
   * @return user instance or null if not found
   */
  User loadUserById(String userId);

  /**
   * Loads user by login name.
   * @param loginName - user login name
   * @return user instance or null if not found
   */
  User loadUserByUsername(String loginName);

  /**
   * Loads user by login token,
   * @param token - user login token
   * @return user instance or null if not found
   */
  User loadUserByToken(String token);

  /**
   * Resolves Apache Shiro permissions for entity and/or target defined by exactly 1 role node!
   * Resolves roles if existing and adds additional metadata to the attributes of the provided user.
   *
   * @param user - previously loaded user
   * @param domainPrefixes the set of domain prefixes to filter permissions by
   */
  User loadUserData(User user, Set<String> domainPrefixes);

  RoleNodePermission updatePermission(String roleNodeId, String roleNodePermissionId, RoleNodeParams params);
}
