package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;


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


  /**
   * Loads role node for provided parameters.
   * NOTE: Parameters must define only 1 role node. Combination target/entity should be unique per role node.
   * @param params - role node target/entity parameters
   * @param mappingContext - mapping context
   * @return found role node or assert error if not found.
   */
  private JpaRoleNode loadRoleNode(RoleNodeParams params, MappingContext mappingContext) {
    RepositoryExecContext<JpaRoleNode> execContext = this.roleNodeRepository.getRepositoryExecContext(mappingContext);
    execContext.setParams(params);
    List<JpaRoleNode> jpaRoleNodes = this.roleNodeRepository.find(execContext);
    assert jpaRoleNodes.size() == 1;
    return jpaRoleNodes.get(0);
  }


  /**
   * Resolves template or instance permissions from role node and adds them to permissions level list.
   * @param roleNode - role node to load permissions for.
   * @param permissionsByLevel - target permissions level list.
   * @param params - default target data.
   * @param currentLevel - hierarchy level counter.
   */
  private void resolveRoleNodePermissionLevels(JpaRoleNode roleNode,
                                               List<List<JpaRoleNodePermission>> permissionsByLevel,
                                               RoleNodeParams params,
                                               int currentLevel
  ) {
    JpaRoleNode loadedRoleNode = this.roleNodeRepository.findById(roleNode.getUuid());
    assert loadedRoleNode != null;
    //if max parent instance level is set and current level is less or equals to it, instance permissions will be taken
    // as opposed to template permissions.
    if (params.getMaxParentInstanceLevel() != null && params.getMaxParentInstanceLevel() >= currentLevel) {
      permissionsByLevel.add(loadedRoleNode.getRoleNodePermissions().stream().filter(p -> {
        if (p instanceof JpaRoleNodePermissionTemplate) {
          return false;
        }
        return true;
      }).toList());
    } else {
      //if not istance permissions are taken from parents, load template permissions for target on current node.
      permissionsByLevel.add(loadedRoleNode.getRoleNodePermissions().stream().filter(p -> {
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
      this.resolveRoleNodePermissionLevels(parentRoleNode, permissionsByLevel, params, currentLevel + 1);
    }
  }


  /**
   * Loads ROOT template permissions for target.
   * @param params - parameters defining target data.
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


  private List<RoleNodePermission> mergePermissionLevels(List<List<JpaRoleNodePermission>> permissionsByLevel) {
    return List.of();
  }


  private List<RoleNodePermission> resolveHierarchyPermissions(JpaRoleNode roleNode,
                                                               Integer maxParentInstanceLevel, MappingContext mapContext) {
    List<List<JpaRoleNodePermission>> permissionsByLevel = new LinkedList<>();
    //add current role node permission instances to 0 level in the list
    permissionsByLevel.add(roleNode.getRoleNodePermissions().stream().filter(p -> {
      if (p instanceof JpaRoleNodePermissionTemplate) {
        return false;
      }
      return true;
    }).toList());
    //preapre target parameters
    RoleNodeParams params = new RoleNodeParams(); //passing target details up the hierarchy
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


  @Override
  public List<RoleNodePermission> loadRoleNodePermissions(RoleNodeParams roleNodeParams) {
    if (roleNodeParams.isEmpty()) {
      throw new ServiceException(ErrorCode.parameter_validation_error, "Role node params cannot be empty");
    }
    MappingContext mapContext = this.roleNodeMapperProvider.getMappingContextForRead();
    //Load role node.
    JpaRoleNode loadedRoleNode = this.loadRoleNode(roleNodeParams, mapContext);
    if (loadedRoleNode.getParent() == null) {
      //return what we have for root node
      List<JpaRoleNodePermission> roleNodePermissions = loadedRoleNode.getRoleNodePermissions().stream().filter(p -> {
        if (p instanceof JpaRoleNodePermissionTemplate) {
          return false;
        }
        return true;
      }).toList();
      return this.roleNodePermissionMapperProvider.getToDtoMapper().toDtos(roleNodePermissions, mapContext);
    }
    //execute magic of hierarchy permission resolving
    return this.resolveHierarchyPermissions(loadedRoleNode, roleNodeParams.getMaxParentInstanceLevel(), mapContext);
  }


  @Override
  public List<Permission> loadPermissions(RoleNodeParams roleNodeParams) {
    return List.of();
  }

}
