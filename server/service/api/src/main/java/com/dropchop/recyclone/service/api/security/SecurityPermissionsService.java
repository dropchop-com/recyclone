package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;

import java.util.List;

public interface SecurityPermissionsService {



  List<RoleNodePermission> loadPermissions(String entity, String entityId);
  List<RoleNodePermission> loadPermissions(RoleNode roleNode);

  List<RoleNodePermission> loadTargetPermissions(String target, String targetId);

}
