package com.dropchop.recyclone.quarkus.it.rest.security;

import com.dropchop.recyclone.base.dto.model.security.*;
import com.google.common.base.Strings;

import java.util.List;
import java.util.UUID;

public class SecurityHelper {

  public static RoleNode roleNodeOf(String uuid, String entity, String entityUuid, Integer maxParentLevelInstsance) {
    RoleNode roleNode = new RoleNode();
    roleNode.setId(uuid);
    roleNode.setTarget(entity);
    roleNode.setEntity(entity);
    roleNode.setEntityId(entityUuid);
    roleNode.setMaxParentInstanceLevel(maxParentLevelInstsance);
    return roleNode;
  }

  public static RoleNode roleNodeOf(String uuid,
                                    String target,
                                    String targetId,
                                    String entity,
                                    String entityUuid,
                                    String entityName,
                                    Integer maxParentLevelInstsance) {
    RoleNode roleNode = new RoleNode();
    roleNode.setId(uuid);
    roleNode.setTarget(target);
    roleNode.setTargetId(targetId);
    roleNode.setEntity(entity);
    roleNode.setEntityId(entityUuid);
    roleNode.setEntityName(entityName);
    roleNode.setMaxParentInstanceLevel(maxParentLevelInstsance);
    return roleNode;
  }

  public static Permission permissionOf(String uuid, String domainCode, String actionCode) {
    Domain domain = new Domain();
    domain.setCode(domainCode);

    Action action = new Action();
    action.setCode(actionCode);

    Permission permission = new Permission();
    permission.setId(uuid);
    permission.setDomain(domain);
    permission.setAction(action);
    permission.setTitle("Permit " + actionCode + " actions on " + domainCode);
    permission.setLang("en");
    return permission;
  }

  public static <P extends RoleNodePermission> P roleNodePermissionOf(Class<P> pClass, String uuid,
                                                                      Permission permission, String target,
                                                                      String targetId, Boolean allowed,
                                                                      RoleNode parent) {
    P roleNodePermission;
    try {
      roleNodePermission = pClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    roleNodePermission.setId(Strings.isNullOrEmpty(uuid) ? UUID.randomUUID().toString() : uuid);
    roleNodePermission.setRoleNode(parent);
    roleNodePermission.setPermission(permission);
    roleNodePermission.setAllowed(allowed);
    if (roleNodePermission instanceof RoleNodePermissionTemplate) {
      ((RoleNodePermissionTemplate) roleNodePermission).setTarget(target);
      ((RoleNodePermissionTemplate) roleNodePermission).setTargetId(targetId);
    }
    return roleNodePermission;
  }

  @SuppressWarnings("SameParameterValue")
  public static  <P extends RoleNodePermission> List<P> prepRoleNodePermissions(Class<P> pClass,
                                                                                RoleNode node,
                                                                                List<Permission> permissions,
                                                                                String target,
                                                                                String targetId) {
    return permissions.stream()
        .map(
            p -> SecurityHelper.roleNodePermissionOf(
                pClass, UUID.randomUUID().toString(), p, target, targetId, true, node
            )
        )
        .toList();
  }
}
