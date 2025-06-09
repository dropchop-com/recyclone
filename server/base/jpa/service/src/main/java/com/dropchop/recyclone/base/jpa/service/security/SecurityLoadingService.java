package com.dropchop.recyclone.base.jpa.service.security;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.mapper.FilteringDtoContext;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.repo.ctx.RepositoryExecContext;
import com.dropchop.recyclone.base.api.service.security.HierarchicalSecurityLoadingService;
import com.dropchop.recyclone.base.dto.model.invoke.Params;
import com.dropchop.recyclone.base.dto.model.invoke.RoleNodeParams;
import com.dropchop.recyclone.base.dto.model.invoke.RoleNodePermissionParams;
import com.dropchop.recyclone.base.dto.model.security.RoleNode;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.jpa.mapper.security.RoleNodePermissionToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.RoleNodeToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.UserToDtoMapper;
import com.dropchop.recyclone.base.jpa.model.security.*;
import com.dropchop.recyclone.base.jpa.repo.BlazeExecContext;
import com.dropchop.recyclone.base.jpa.repo.security.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
public class SecurityLoadingService extends HierarchicalSecurityLoadingService
    implements com.dropchop.recyclone.base.api.service.security.SecurityLoadingService {

  @Inject
  UserRepository userRepository;

  @Inject
  UserAccountRepository userAccountRepository;

  @Inject
  UserMapperProvider userMapperProvider;

  @Inject
  RoleNodeRepository roleNodeRepository;

  @Inject
  RoleNodeMapperProvider roleNodeMapperProvider;

  @Inject
  RoleNodePermissionRepository roleNodePermissionRepository;

  @Inject
  RoleNodePermissionMapperProvider roleNodePermissionMapperProvider;

  @Inject
  PermissionRepository permissionRepository;

  /**
   * Loads role node permissions
   * @param roleNode  - defines role node from where permissions must be loaded
   * @param roleNodeParams - additional search parameters for search. (Example: different target than role node)
   */
  private void loadRoleNodePermissions(RoleNode roleNode, RoleNodeParams roleNodeParams) {
    MappingContext permMapContext = this.roleNodePermissionMapperProvider.getMappingContextForRead();
    BlazeExecContext<JpaRoleNodePermission> permissionContext =
        this.roleNodePermissionRepository.getRepositoryExecContext(permMapContext);
    RoleNodePermissionToDtoMapper permToDtoMapper = this.roleNodePermissionMapperProvider.getToDtoMapper();

    RoleNodePermissionParams params = new RoleNodePermissionParams();
    params.setRoleNodeId(roleNode.getId());
    if (roleNodeParams != null) { //check of targets are different and load permissions on role node for that target
      String target = roleNodeParams.getTarget();
      String targetId = roleNodeParams.getTargetId();
      if (target != null && !target.isBlank() && !roleNode.getTarget().equals(target)) {
        params.setTarget(target);
        if (targetId != null && !targetId.isBlank()) {
          params.setTargetId(targetId);
        }
      }
    }
    permissionContext.setParams(params);
    List<JpaRoleNodePermission> permissions = this.roleNodePermissionRepository.find(permissionContext);
    List<RoleNodePermission> permissionDtos = permToDtoMapper.toDtos(permissions, permMapContext);
    roleNode.setRoleNodePermissions(permissionDtos);
  }


  /**
   * Loads role node for provided parameters.
   * NOTE: Parameters must define only 1 role node. Combination target/entity should be unique per role node.
   *
   * @param params         - role node target/entity parameters
   * @return found role node or service exception if not found.
   */
  @Override
  protected RoleNode loadRoleNode(RoleNodeParams params, RoleNodeFlags flags) {
    MappingContext mapContext = this.roleNodeMapperProvider.getMappingContextForRead();
    RepositoryExecContext<JpaRoleNode> execContext = this.roleNodeRepository.getRepositoryExecContext(mapContext);
    execContext.setParams(params);
    List<JpaRoleNode> jpaRoleNodes = this.roleNodeRepository.find(execContext);

    if (jpaRoleNodes == null || jpaRoleNodes.isEmpty()) {
      if (flags.isMustExist()) {
        throw new ServiceException(getStatusMessage("Role node not found for params", params));
      } else {
        return null;
      }
    }
    if (jpaRoleNodes.size() != 1) {
      throw new ServiceException(getStatusMessage("Only one role node must be found by params", params));
    }
    RoleNodeToDtoMapper roleNodeToDtoMapper = this.roleNodeMapperProvider.getToDtoMapper();
    RoleNode roleNode = roleNodeToDtoMapper.toDto(jpaRoleNodes.getFirst(), mapContext);
    if (flags.isWithPermissions()) {
      loadRoleNodePermissions(roleNode, params);
    }
    return roleNode;
  }

  /**
   * Loads role node for provided uuid.
   *
   * @param uuid         - role node uuid
   * @return found role node or service exception if not found.
   */
  @Override
  protected RoleNode loadRoleNodeById(UUID uuid) {
    MappingContext mapContext = this.roleNodeMapperProvider.getMappingContextForRead();
    RoleNodeToDtoMapper roleNodeToDtoMapper = this.roleNodeMapperProvider.getToDtoMapper();
    JpaRoleNode loadedParentRoleNode = this.roleNodeRepository.findById(uuid);
    RoleNode roleNode = roleNodeToDtoMapper.toDto(loadedParentRoleNode, mapContext);
    loadRoleNodePermissions(roleNode, null);
    return roleNode;
  }


  @Override
  protected RoleNodePermission loadRoleNodePermissionById(UUID uuid) {
    MappingContext mapContext = this.roleNodePermissionMapperProvider.getMappingContextForRead();
    RoleNodePermissionToDtoMapper roleNodeToDtoMapper = this.roleNodePermissionMapperProvider.getToDtoMapper();
    JpaRoleNodePermission jpaPermission = this.roleNodePermissionRepository.findById(uuid);
    return roleNodeToDtoMapper.toDto(jpaPermission, mapContext);
  }


  @Override
  protected void deleteRoleNodePermission(UUID uuid) {
    JpaRoleNodePermission jpaPermission = this.roleNodePermissionRepository.findById(uuid);
    this.roleNodePermissionRepository.delete(jpaPermission);
  }


  @Override
  protected void updateRoleNodePermissionAllowed(UUID uuid, boolean allowed) {
    JpaRoleNodePermission jpaPermission = this.roleNodePermissionRepository.findById(uuid);
    jpaPermission.setAllowed(allowed);
    this.roleNodePermissionRepository.save(jpaPermission);
  }


  @Override
  protected void createRoleNodePermission(UUID targetRoleNodeId, RoleNodePermission sourceRoleNodePermission) {
    UUID permissionUuid = UUID.fromString(sourceRoleNodePermission.getPermission().getId());
    boolean isAllowed = !sourceRoleNodePermission.getAllowed();

    JpaRoleNode jpaRoleNode = this.roleNodeRepository.findById(targetRoleNodeId);
    boolean isTemplateRoleNode = jpaRoleNode.getEntity() == null || jpaRoleNode.getEntity().isBlank();

    JpaPermission jpaPermission = this.permissionRepository.findById(permissionUuid);

    JpaRoleNodePermission jpaRoleNodePermission = isTemplateRoleNode ?
      new JpaRoleNodePermissionTemplate() : new JpaRoleNodePermission();

    if (jpaRoleNodePermission instanceof JpaRoleNodePermissionTemplate) {
      ((JpaRoleNodePermissionTemplate)jpaRoleNodePermission).setTarget(jpaRoleNode.getTarget());
      ((JpaRoleNodePermissionTemplate)jpaRoleNodePermission).setTargetId(jpaRoleNode.getTargetId());
    }

    jpaRoleNodePermission.setUuid(UUID.randomUUID());
    jpaRoleNodePermission.setRoleNode(jpaRoleNode);
    jpaRoleNodePermission.setPermission(jpaPermission);
    jpaRoleNodePermission.setAllowed(isAllowed);
    jpaRoleNodePermission.setCreated(ZonedDateTime.now());
    jpaRoleNodePermission.setModified(ZonedDateTime.now());

    this.roleNodePermissionRepository.save(jpaRoleNodePermission);
  }


  /**
   * Maps loaded jsa user to dto with user accounts.
   * @param user - jsa user instance.
   * @return user dto or null if not loaded.
   */
  private User mapToUser(JpaUser user) {
    if (user == null) {
      return null;
    }
    Params params = new Params();
    params.getFilter().getContent().setTreeLevel(5);
    MappingContext mapContext = new FilteringDtoContext();
    mapContext.setParams(params);
    UserToDtoMapper userToDtoMapper = this.userMapperProvider.getToDtoMapper();
    return userToDtoMapper.toDto(user, mapContext);
  }

  @Override
  public User loadUserByToken(String token) {
    JpaUserAccount userAccount = userAccountRepository.findByToken(token);
    if (userAccount == null) {return null;}
    return this.mapToUser(userAccount.getUser());
  }

  @Override
  public User loadUserByUsername(String loginName) {
    JpaUserAccount userAccount = userAccountRepository.findByLoginName(loginName);
    if (userAccount == null) {return null;}
    return this.mapToUser(userAccount.getUser());
  }
}
