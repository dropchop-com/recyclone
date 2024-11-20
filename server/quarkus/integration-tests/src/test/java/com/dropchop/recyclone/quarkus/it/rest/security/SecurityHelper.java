package com.dropchop.recyclone.quarkus.it.rest.security;

import com.dropchop.recyclone.model.dto.security.*;
import com.google.common.base.Strings;

import java.util.UUID;

public class SecurityHelper {

  public static RoleNode roleNodeOf(String uuid, String entity, String entityUuid) {
    RoleNode roleNode = new RoleNode();
    roleNode.setUuid(uuid);
    roleNode.setTarget(entity);
    roleNode.setEntity(entity);
    roleNode.setEntityId(entityUuid);
    return roleNode;
  }


  public static RoleNode roleNodeOf(String uuid, String target, String targetId, String entity, String entityUuid) {
    RoleNode roleNode = new RoleNode();
    roleNode.setUuid(uuid);
    roleNode.setTarget(target);
    roleNode.setTargetId(targetId);
    roleNode.setEntity(entity);
    roleNode.setEntityId(entityUuid);
    return roleNode;
  }



  public static Permission permissionOf(String uuid, String domainCode, String actionCode) {
    Domain domain = new Domain();
    domain.setCode(domainCode);

    Action action = new Action();
    action.setCode(actionCode);

    Permission permission = new Permission();
    permission.setUuid(uuid);
    permission.setDomain(domain);
    permission.setAction(action);
    permission.setTitle("Permit " + actionCode + " actions on " + domainCode);
    permission.setLang("en");
    return permission;
  }

  public static RoleNodePermission roleNodePermissionOf(String uuid,
                                                        Permission permission,
                                                        String target,
                                                        String targetId,
                                                        Boolean allowed,
                                                        RoleNode parent,
                                                        boolean asTemplate
  ) {
    RoleNodePermission roleNodePermission = asTemplate ? new RoleNodePermissionTemplate() : new RoleNodePermission();
    roleNodePermission.setUuid(Strings.isNullOrEmpty(uuid) ? UUID.randomUUID().toString() : uuid);
    roleNodePermission.setRoleNode(parent);
    roleNodePermission.setPermission(permission);
    roleNodePermission.setAllowed(allowed);
    if (roleNodePermission instanceof RoleNodePermissionTemplate) {
      ((RoleNodePermissionTemplate) roleNodePermission).setTarget(target);
      ((RoleNodePermissionTemplate) roleNodePermission).setTargetId(targetId);
    }
    return roleNodePermission;
  }

}
