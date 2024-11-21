package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.attr.AttributeBool;
import com.dropchop.recyclone.model.api.attr.AttributeDecimal;
import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.invoke.StatusMessage;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermissionTemplate;
import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextBinder;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodeMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodePermissionMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodePermissionRepository;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodeRepository;
import com.dropchop.recyclone.service.api.RecycloneType;
import com.dropchop.recyclone.service.api.Service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;
import static com.dropchop.recyclone.model.api.security.Constants.Domains.Security.PERMISSION;

@Slf4j
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
public class SecurityLoadingService implements com.dropchop.recyclone.service.api.security.SecurityLoadingService {


  @Inject
  RoleNodeRepository roleNodeRepository;

  @Inject
  RoleNodePermissionRepository roleNodePermissionRepository;

  @Inject
  RoleNodeMapperProvider roleNodeMapperProvider;

  @Inject
  RoleNodePermissionMapperProvider roleNodePermissionMapperProvider;

  @Inject
  ExecContextBinder execContextBinder;


  private static StatusMessage getStatusMessage(String error, RoleNodeParams params) {
    StatusMessage status = new StatusMessage(ErrorCode.data_validation_error, error);
    if (params != null) {
      status.setDetails(Set.of(
        new AttributeString("roleNodeId", params.getIdentifiers() != null ? params.getIdentifiers().toString() : ""),
        new AttributeString("target", params.getTarget() != null ? params.getTarget() : ""),
        new AttributeString("targetId", params.getTargetId() != null ? params.getTargetId() : ""),
        new AttributeString("entity", params.getEntity() != null ? params.getEntity() : ""),
        new AttributeString("entityId", params.getEntityId() != null ? params.getEntityId() : ""),
        new AttributeBool("all", params.getAll() != null ? params.getAll() : false),
        new AttributeDecimal("maxParentInstanceLevel",
          params.getMaxParentInstanceLevel() != null ? params.getMaxParentInstanceLevel() : -1)
      ));
    }
    return status;
  }


  /**
   * Loads role node for provided parameters.
   * NOTE: Parameters must define only 1 role node. Combination target/entity should be unique per role node.
   *
   * @param params         - role node target/entity parameters
   * @param mappingContext - mapping context
   * @return found role node or service exception if not found.
   */
  private JpaRoleNode loadRoleNode(RoleNodeParams params, MappingContext mappingContext) {
    RepositoryExecContext<JpaRoleNode> execContext = this.roleNodeRepository.getRepositoryExecContext(mappingContext);
    execContext.setParams(params);
    List<JpaRoleNode> jpaRoleNodes = this.roleNodeRepository.find(execContext);
    if (jpaRoleNodes == null || jpaRoleNodes.isEmpty()) {
      throw new ServiceException(getStatusMessage("Role node not found for params", params));
    }
    if (jpaRoleNodes.size() != 1) {
      throw new ServiceException(getStatusMessage("Only one role node must be found by params", params));
    }
    return jpaRoleNodes.get(0);
  }


  /**
   * Resolves template or instance permissions from role node and adds them to permissions level list.
   *
   * @param roleNode           - role node to load permissions for.
   * @param permissionsByLevel - target permissions level list.
   * @param params             - default target data.
   * @param currentLevel       - hierarchy level counter.
   */
  private void resolveRoleNodePermissionLevels(JpaRoleNode roleNode,
                                               List<List<JpaRoleNodePermission>> permissionsByLevel,
                                               RoleNodeParams params,
                                               int currentLevel
  ) {
    if (roleNode == null) {
      throw new ServiceException(getStatusMessage("Role node cannot be null", null));
    }
    //if max parent instance level is set and current level is less or equals to it, instance permissions will be taken
    // as opposed to template permissions.
    if (params.getMaxParentInstanceLevel() != null && params.getMaxParentInstanceLevel() >= currentLevel) {
      permissionsByLevel.add(roleNode.getRoleNodePermissions().stream().filter(p -> {
        if (p instanceof JpaRoleNodePermissionTemplate) {
          return false;
        }
        return true;
      }).toList());
    } else {
      //if instance permissions are not taken from parents, load template permissions for target on current node.
      permissionsByLevel.add(roleNode.getRoleNodePermissions().stream().filter(p -> {
        if (p instanceof JpaRoleNodePermissionTemplate permissionTemplate) {
          return permissionTemplate.getTarget().equals(params.getTarget())
            && permissionTemplate.getTargetId().equals(params.getTargetId());
        }
        return false;
      }).toList());
    }
    //find parent node and repeat the process until no more parents.
    JpaRoleNode parentRoleNode = roleNode.getParent();
    if (parentRoleNode != null) {
      JpaRoleNode loadedParentRoleNode = this.roleNodeRepository.findById(parentRoleNode.getUuid());
      this.resolveRoleNodePermissionLevels(loadedParentRoleNode, permissionsByLevel, params, currentLevel + 1);
    }
  }


  /**
   * Loads ROOT template permissions for target.
   *
   * @param params             - parameters defining target data.
   * @param permissionsByLevel - list of permissions by level that root template permissions will be added to.
   */
  private void resolveRootTargetPermission(RoleNodeParams params,
                                           List<List<JpaRoleNodePermission>> permissionsByLevel,
                                           MappingContext mapContext) {
    JpaRoleNode loadedRoleNode = this.loadRoleNode(params, mapContext);
    permissionsByLevel.add(loadedRoleNode.getRoleNodePermissions().stream().filter(p -> {
      if (p instanceof JpaRoleNodePermissionTemplate permissionTemplate) {
        return permissionTemplate.getTarget().equals(params.getTarget())
          && permissionTemplate.getTargetId().equals(params.getTargetId());
      }
      return false;
    }).toList());
  }


  /**
   * Merges role node permissions levels hierarchy into flat list of resolved role node permissions.
   * This method expects that permission levels are reverted, meaning the 1st level (at index 0) holds the permissions of starting role node.
   * Each level (index +1) then holds permissions from parent role node.
   * So to make code mode readable list of permission levels is reversed so the 1st level (at index 0) has ROOT role node permissions
   * and each level then has its child role node permissions. Last level has the permissions of starting role node.
   * When merging permissions each level (n+1) overrides previous level permissions (if in the list).
   *
   * @param permissionsByLevel - list of permissions for each role node in the starting role node hierarchy.
   * @return collection of permissions resolved from hierarchy.
   */
  private Collection<JpaRoleNodePermission> mergePermissionLevels(List<List<JpaRoleNodePermission>> permissionsByLevel) {
    if (permissionsByLevel == null) {
      throw new ServiceException(getStatusMessage("Role node permissions levels cannot be null", null));
    }
    if (permissionsByLevel.isEmpty()) {
      return Collections.emptyList();
    }
    Map<UUID, JpaRoleNodePermission> resolvedPermissions = new LinkedHashMap<>();
    Collections.reverse(permissionsByLevel);
    permissionsByLevel.forEach(levelPermissions -> {
      levelPermissions.forEach(roleNodePermission -> {
        UUID permissionUuid = roleNodePermission.getPermission().getUuid();
        resolvedPermissions.put(permissionUuid, roleNodePermission);
      });
    });
    return resolvedPermissions.values();
  }


  /**
   * Main method to start hierarchy permission resolving for loaded role node.
   * @param roleNode                - role node to start resolving on
   * @param maxParentInstanceLevel  - maximum level of instance permission taken from parents that influence end permission state
   * @param mapContext              - passing mapping context for convenience.
   * @return list or merged role node permissions of all role nodes in the hierarchy.
   */
  private Collection<JpaRoleNodePermission> resolveHierarchyPermissions(JpaRoleNode roleNode,
                                                               Integer maxParentInstanceLevel, MappingContext mapContext) {
    List<List<JpaRoleNodePermission>> permissionsByLevel = new LinkedList<>();
    //prepare target parameters
    RoleNodeParams params = new RoleNodeParams(); //will be passing original target details up the hierarchy
    params.setTarget(roleNode.getTarget());
    params.setTargetId(roleNode.getTargetId());
    params.setMaxParentInstanceLevel(maxParentInstanceLevel);
    //resolve all hierarchy permissions from parent nodes and add permissions into permission levels list.
    this.resolveRoleNodePermissionLevels(roleNode, permissionsByLevel, params, 1);
    //resolve root level template permissions and add them as last level in permission levels list.
    this.resolveRootTargetPermission(params, permissionsByLevel, mapContext);
    //merge all permission levels into final list or resolved permissions.
    return this.mergePermissionLevels(permissionsByLevel);
  }


  private boolean isInstanceOnlyRoleNode(JpaRoleNode loadedRoleNode) {
    return loadedRoleNode.getRoleNodePermissions().stream()
      .filter(p -> p instanceof JpaRoleNodePermissionTemplate)
      .findFirst()
      .orElse(null) != null;
  }


  @Transactional
  @Override
  public List<RoleNodePermission> loadRoleNodePermissions(RoleNodeParams roleNodeParams) {
    if (roleNodeParams.isEmpty()) {
      throw new ServiceException(ErrorCode.parameter_validation_error, "Role node params cannot be empty");
    }
    MappingContext mapContext = this.roleNodeMapperProvider.getMappingContextForRead();
    //Load role node.
    JpaRoleNode loadedRoleNode = this.loadRoleNode(roleNodeParams, mapContext);
    if (this.isInstanceOnlyRoleNode(loadedRoleNode)) {
      //return what we have for root node
      return this.roleNodePermissionMapperProvider.getToDtoMapper().toDtos(loadedRoleNode.getRoleNodePermissions(), mapContext);
    }
    //execute magic of hierarchy permission resolving
    Collection<JpaRoleNodePermission> resolvedPermissions = this.resolveHierarchyPermissions(
      loadedRoleNode, roleNodeParams.getMaxParentInstanceLevel(), mapContext);
    return this.roleNodePermissionMapperProvider.getToDtoMapper().toDtos(resolvedPermissions, mapContext);
  }


  @Transactional
  @Override
  public List<Permission> loadPermissions(RoleNodeParams roleNodeParams) {
    Boolean all = roleNodeParams.getAll();
    List<RoleNodePermission> roleNodePermissions = this.loadRoleNodePermissions(roleNodeParams);
    List<Permission> permissions = new LinkedList<>();
    roleNodePermissions.forEach(roleNodePermission -> {
      if ((all != null && all) || roleNodePermission.getAllowed() ) {
        permissions.add(roleNodePermission.getPermission());
      }
    });
    return permissions;
  }

}
