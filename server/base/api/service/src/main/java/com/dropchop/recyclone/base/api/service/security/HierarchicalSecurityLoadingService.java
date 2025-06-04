package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.model.attr.AttributeBool;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.invoke.StatusMessage;
import com.dropchop.recyclone.base.dto.model.invoke.RoleNodeParams;
import com.dropchop.recyclone.base.dto.model.security.Permission;
import com.dropchop.recyclone.base.dto.model.security.RoleNode;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermissionTemplate;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 22. 11. 24.
 */
@Slf4j
@Getter
abstract public class HierarchicalSecurityLoadingService implements SecurityLoadingService {

  protected static StatusMessage getStatusMessage(String error, RoleNodeParams params) {
    StatusMessage status = new StatusMessage(ErrorCode.data_validation_error, error);
    if (params != null) {
      status.setDetails(Set.of(
        new AttributeString(
          "roleNodeId", params.getIdentifiers() != null ? params.getIdentifiers().toString() : ""
        ),
        new AttributeString(
          "target", params.getTarget() != null ? params.getTarget() : ""
        ),
        new AttributeString(
          "targetId", params.getTargetId() != null ? params.getTargetId() : ""
        ),
        new AttributeString(
          "entity", params.getEntity() != null ? params.getEntity() : ""
        ),
        new AttributeString(
          "entityId", params.getEntityId() != null ? params.getEntityId() : ""
        ),
        new AttributeBool(
          "all", params.getAll() != null ? params.getAll() : false
        )
      ));
    }
    return status;
  }


  /**
   * Resolves template or instance permissions from role node and adds them to permissions level list.
   *
   * @param roleNode           - role node to load permissions for.
   * @param permissionsByLevel - target permissions level list.
   * @param params             - default target data.
   * @param currentLevel       - hierarchy level counter.
   */
  private void resolveRoleNodePermissionLevels(RoleNode roleNode,
                                               List<List<RoleNodePermission>> permissionsByLevel,
                                               RoleNodeParams params,
                                               int currentLevel,
                                               Integer maxParentInstanceLevel
  ) {
    if (roleNode == null) {
      throw new ServiceException(getStatusMessage("Role node cannot be null", null));
    }
    //if role node is instance and max parent instance level is set and current level is less or equals to it, instance permissions will be taken
    // as opposed to template permissions.
    if (maxParentInstanceLevel != null
        && maxParentInstanceLevel > 0
        && maxParentInstanceLevel >= currentLevel
    ) {
      permissionsByLevel.add(roleNode.getRoleNodePermissions().stream()
        .filter(p -> !(p instanceof RoleNodePermissionTemplate))
        .toList());
    } else {
      //if instance permissions are not taken from parents, load template permissions for target on current node.
      permissionsByLevel.add(roleNode.getRoleNodePermissions().stream().filter(p -> {
        if (p instanceof RoleNodePermissionTemplate permissionTemplate) {
          String targetId = permissionTemplate.getTargetId();
          return permissionTemplate.getTarget().equals(params.getTarget())
            && (targetId == null || targetId.equals(params.getTargetId()));
        }
        return false;
      }).toList());
    }
    //find parent node and repeat the process until no more parents.
    RoleNode parentRoleNode = roleNode.getParent();
    if (parentRoleNode != null) {
      RoleNode loadedParentRoleNode = this.loadRoleNodeById(parentRoleNode.getUuid());
      this.resolveRoleNodePermissionLevels(
        loadedParentRoleNode, permissionsByLevel, params, currentLevel + 1, maxParentInstanceLevel
      );
    }
  }


  /**
   * Loads ROOT template permissions for target.
   *
   * @param params             - parameters defining target data.
   * @param permissionsByLevel - list of permissions by level that root template permissions will be added to.
   */
  private void resolveRootTargetPermission(RoleNodeParams params, List<List<RoleNodePermission>> permissionsByLevel) {
    RoleNodeParams rootParams = RoleNodeParams.of(params);
    rootParams.setRootOnly(true);
    RoleNode loadedRoleNode = this.loadRoleNode(rootParams);
    permissionsByLevel.add(loadedRoleNode.getRoleNodePermissions().stream().filter(p -> {
      if (p instanceof RoleNodePermissionTemplate permissionTemplate) {
        String targetId = permissionTemplate.getTargetId();
        return permissionTemplate.getTarget().equals(rootParams.getTarget())
          && (targetId == null || targetId.equals(rootParams.getTargetId()));
      }
      return false;
    }).toList());
  }


  /**
   * Merges role node permissions levels hierarchy into flat list of resolved role node permissions.
   * This method expects that permission levels are reverted, meaning the 1st level (at index 0) holds
   * the permissions of starting role node.
   * Each level (index +1) then holds permissions from parent role node.
   * So to make code mode readable list of permission levels is reversed so the 1st level (at index 0) has ROOT
   * role node permissions and each level then has its child role node permissions.
   * Last level has the permissions of starting role node.
   * When merging permissions each level (n+1) overrides previous level permissions (if in the list).
   *
   * @param permissionsByLevel - list of permissions for each role node in the starting role node hierarchy.
   * @return collection of permissions resolved from hierarchy.
   */
  private Collection<RoleNodePermission> mergePermissionLevels(List<List<RoleNodePermission>> permissionsByLevel) {
    if (permissionsByLevel == null) {
      throw new ServiceException(
        ErrorCode.data_validation_error,
        "Role node permissions levels cannot be null"
      );
    }
    if (permissionsByLevel.isEmpty()) {
      return Collections.emptyList();
    }
    Map<UUID, RoleNodePermission> resolvedPermissions = new LinkedHashMap<>();
    Collections.reverse(permissionsByLevel);
    permissionsByLevel.forEach(levelPermissions ->
      levelPermissions.forEach(roleNodePermission -> {
        UUID permissionUuid = roleNodePermission.getPermission().getUuid();
        resolvedPermissions.put(permissionUuid, roleNodePermission);
      })
    );
    return resolvedPermissions.values();
  }


  /**
   * Main method to start hierarchy permission resolving for loaded role node.
   *
   * @param roleNode               - role node to start resolving on
   * @param maxParentInstanceLevel - maximum level of instance permission taken from parents
   *                               that influence end permission state
   * @return list or merged role node permissions of all role nodes in the hierarchy.
   */
  private Collection<RoleNodePermission> resolveHierarchyPermissions(RoleNode roleNode,
                                                                     Integer maxParentInstanceLevel
  ) {
    List<List<RoleNodePermission>> permissionsByLevel = new LinkedList<>();
    //prepare target parameters
    RoleNodeParams params = new RoleNodeParams(); //will be passing original target details up the hierarchy
    params.setTarget(roleNode.getTarget());
    params.setTargetId(roleNode.getTargetId());
    //resolve all hierarchy permissions from parent nodes and add permissions into permission levels list.
    this.resolveRoleNodePermissionLevels(roleNode, permissionsByLevel, params, 0, maxParentInstanceLevel);
    //resolve root level template permissions and add them as last level in permission levels list.
    this.resolveRootTargetPermission(params, permissionsByLevel);
    //merge all permission levels into final list or resolved permissions.
    return this.mergePermissionLevels(permissionsByLevel);
  }


  private boolean isInstanceRoleNode(RoleNode loadedRoleNode) {
    return loadedRoleNode.getEntity() != null && !loadedRoleNode.getEntity().isBlank()
      && loadedRoleNode.getEntityId() != null && !loadedRoleNode.getEntityId().isBlank();
  }


  @Transactional
  @Override
  public Collection<RoleNodePermission> loadRoleNodePermissions(RoleNodeParams roleNodeParams) {
    if (roleNodeParams.isEmpty()) {
      throw new ServiceException(ErrorCode.parameter_validation_error, "Role node params cannot be empty");
    }
    //Load role node.
    RoleNode loadedRoleNode = this.loadRoleNode(roleNodeParams);
    if (!this.isInstanceRoleNode(loadedRoleNode) && loadedRoleNode.getParent() == null) {
      //return what we have for root node
      return loadedRoleNode.getRoleNodePermissions();
    }
    //execute magic of hierarchy permission resolving
    return this.resolveHierarchyPermissions(loadedRoleNode, loadedRoleNode.getMaxParentInstanceLevel());
  }


  @Transactional
  @Override
  public Collection<Permission> loadPermissions(RoleNodeParams roleNodeParams) {
    Boolean all = roleNodeParams.getAll();
    Collection<RoleNodePermission> roleNodePermissions = this.loadRoleNodePermissions(roleNodeParams);
    List<Permission> permissions = new LinkedList<>();
    roleNodePermissions.forEach(roleNodePermission -> {
      if ((all != null && all) || roleNodePermission.getAllowed()) {
        permissions.add(roleNodePermission.getPermission());
      }
    });
    return permissions;
  }


  @Transactional
  @Override
  public RoleNodePermission updatePermission(String roleNodeId, String roleNodePermissionId) {

    UUID roleNodeUuid = UUID.fromString(roleNodeId);
    UUID permissionUuid = UUID.fromString(roleNodePermissionId);

    RoleNode roleNode = this.loadRoleNodeById(roleNodeUuid);
    RoleNodePermission roleNodePermission = this.loadRoleNodePermissionById(permissionUuid);

    boolean isRootRoleNode = roleNode.getEntity() == null && roleNode.getParent() == null;
    boolean isRoleNodeTemplate = roleNode.getEntity() == null || roleNode.getEntity().isBlank();
    boolean isPermissionTemplate = roleNodePermission instanceof RoleNodePermissionTemplate;
    boolean isSameRoleNode = roleNode.getId().equals(roleNodePermission.getRoleNode().getId());
    boolean isSameRoleNodeTarget = roleNode.getTarget().equals(roleNodePermission.getRoleNode().getTarget());
    boolean isAllowed = !roleNodePermission.getAllowed();

    log.info(
      "Resolved permission process parameters " +
      "isRootRoleNode [{}] isRoleNodeTemplate [{}] isPermissionTemplate [{}] isSameRoleNode [{}]" +
      "isSameRoleNodeTarget [{}] isAllowed [{}]",
      isRootRoleNode, isRoleNodeTemplate, isPermissionTemplate, isSameRoleNode,
      isSameRoleNodeTarget, isAllowed
    );

    if (isRoleNodeTemplate) {
      if (isSameRoleNode) {
        if (isRootRoleNode) {
          //update  permission allowed flag changing the permission of root role node !!!
          log.info("Will change allowed flag on permission [{}] allowed from [{}] to [{}]",
            permissionUuid, roleNodePermission.getAllowed(), isAllowed);
          this.updateRoleNodePermissionAllowed(permissionUuid, isAllowed);
        } else {
          //delete permission from current role node if role node is template role node but not root role node
          log.info("Will delete permission [{}] from template role node [{}]",
            permissionUuid, roleNodeUuid);
          this.deleteRoleNodePermission(permissionUuid);
        }
      } else {
        //when role nodes are different add inverse allowed flagged permission to current role node
        //NOTE: prepare template role node if not same target and add permission to it!
        log.info("Will add new permission from [{}] from template role node [{}]",
          permissionUuid, roleNodeUuid);
        this.createRoleNodePermission(roleNodeUuid, roleNodePermission);
      }
    } else { //working with real entity
      if (isSameRoleNode) {
        if (!isPermissionTemplate) {
          //delete instance permission from current entity role node
          log.info("Will delete permission [{}] from instance role node [{}]",
            permissionUuid, roleNodeUuid);
          this.deleteRoleNodePermission(permissionUuid);
        }
      }  else {
        //when role node different from permission role node add opposite permission to current role node
        if (isPermissionTemplate) {
          //create template
        }
      }
    }
    return new RoleNodePermission();
  }


  /**
   * Loads role node for provided parameters.
   * NOTE: Parameters must define only 1 role node. Combination target/entity should be unique per role node.
   *
   * @param params - role node target/entity parameters
   * @return found role node or service exception if not found.
   */
  abstract protected RoleNode loadRoleNode(RoleNodeParams params);


  /**
   * Loads role node for provided uuid.
   *
   * @param uuid - role node uuid
   * @return found role node or service exception if not found.
   */
  abstract protected RoleNode loadRoleNodeById(UUID uuid);

  /**
   * Loads role node for provided uuid.
   *
   * @param uuid - role node uuid
   * @return found role node or service exception if not found.
   */
  abstract protected RoleNodePermission loadRoleNodePermissionById(UUID uuid);


  abstract protected void deleteRoleNodePermission(UUID uuid);
  abstract protected void updateRoleNodePermissionAllowed(UUID uuid, boolean allowed);

  /**
   * Uses source roleNodePermission to create a new instance with reverse allowed value
   * and attaches it to specified role node
   *
   * @param targetRoleNodeId  - target role node to attach permission to
   * @param sourceRoleNodePermission - existing role node permission to create new role node permission from
   */
  abstract protected void createRoleNodePermission(
    UUID targetRoleNodeId, RoleNodePermission sourceRoleNodePermission);
}
