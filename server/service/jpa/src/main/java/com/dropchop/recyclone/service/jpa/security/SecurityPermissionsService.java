package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodePermissionRepository;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodeRepository;
import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.List;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;


@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
public class SecurityPermissionsService implements com.dropchop.recyclone.service.api.security.SecurityPermissionsService {

  @Inject
  RoleNodeRepository roleNodeRepository;


  @Inject
  RoleNodePermissionRepository roleNodePermissionRepository;


  @Override
  public List<RoleNodePermission> loadPermissions(String entity, String entityId) {
    return List.of();
  }


  @Override
  public List<RoleNodePermission> loadPermissions(RoleNode roleNode) {
    return List.of();
  }


  @Override
  public List<RoleNodePermission> loadTargetPermissions(String target, String targetId) {
    return List.of();
  }
}
