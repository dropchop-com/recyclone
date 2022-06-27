package com.dropchop.shiro.utils;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.dto.security.PermissionInstance;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Resolve permissions for subject or subjects of subject, thus allowing permission hierarchy (parent - child)
 */
public interface Resolver {

  <M extends Model> List<Permission> resolve(Class<M> subject, UUID identifier, Class<M> subSubject, UUID subSubjectIdentifier);


  /**
   * Merges permissions from top to bottom (first list to last list)
   * @param permissionsList contains lists of permissions for each hierarchy level.
   * @return list of UUIDs of allowed permissions.
   */
  default List<UUID> mergePermissions(List<List<? extends PermissionInstance>> permissionsList) {

    Map<UUID, Boolean> resolvedPermissions = new LinkedHashMap<>();
    for (List<? extends PermissionInstance> levelPermissions : permissionsList) {
      for(PermissionInstance pi : levelPermissions) {
        resolvedPermissions.put(pi.getPermissionId(), pi.getAllowed());
      }
    }
    return resolvedPermissions.entrySet().stream().filter( (e) -> {return e.getValue();}).map(e -> e.getKey()).collect(Collectors.toList());
  }
}
