package com.dropchop.shiro.utils;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.dto.model.security.PermissionInstance;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Resolve permissions for subject or subjects of subject, thus allowing permission hierarchy (parent - child)
 */
@SuppressWarnings("unused")
public interface Resolver {

  <M extends Model> void resolve(Class<M> subject, UUID identifier, Class<M> subSubject, UUID subSubjectIdentifier,
                                 List<List<? extends PermissionInstance>> targetList);


  /**
   * Merges permissions from top to bottom (first list to last list)
   * @param permissionsList contains lists of permissions for each hierarchy level.
   * @return list of UUIDs of allowed permissions.
   */
  default List<UUID> mergePermissions(List<List<? extends PermissionInstance>> permissionsList, boolean allowedOnly) {

    Map<UUID, Boolean> resolvedPermissions = new LinkedHashMap<>();
    for (List<? extends PermissionInstance> levelPermissions : permissionsList) {
      for(PermissionInstance pi : levelPermissions) {
        resolvedPermissions.put(pi.getPermission().getUuid(), pi.getAllowed());
      }
    }
    if (allowedOnly) {
      //filter allowed only permissions
      return resolvedPermissions.entrySet().stream()
          .filter(Map.Entry::getValue)
          .map(Map.Entry::getKey)
          .collect(Collectors.toList());
    }
    return new ArrayList<>(resolvedPermissions.keySet());
  }
}
