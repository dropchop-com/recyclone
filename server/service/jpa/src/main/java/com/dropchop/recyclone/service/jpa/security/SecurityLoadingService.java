package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.jpa.security.RoleNodeToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.UserToDtoMapper;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUser;
import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextBinder;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.jpa.blaze.security.*;
import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;

@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
public class SecurityLoadingService extends com.dropchop.recyclone.service.security.common.SecurityLoadingService {

  @Inject
  UserRepository userRepository;

  @Inject
  UserMapperProvider userMapperProvider;

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
   *
   * @param params         - role node target/entity parameters
   * @return found role node or service exception if not found.
   */
  @Override
  protected RoleNode loadRoleNode(RoleNodeParams params) {
    MappingContext mapContext = this.roleNodeMapperProvider.getMappingContextForRead();
    RepositoryExecContext<JpaRoleNode> execContext = this.roleNodeRepository.getRepositoryExecContext(mapContext);
    execContext.setParams(params);
    List<JpaRoleNode> jpaRoleNodes = this.roleNodeRepository.find(execContext);
    if (jpaRoleNodes == null || jpaRoleNodes.isEmpty()) {
      throw new ServiceException(getStatusMessage("Role node not found for params", params));
    }
    if (jpaRoleNodes.size() != 1) {
      throw new ServiceException(getStatusMessage("Only one role node must be found by params", params));
    }
    RoleNodeToDtoMapper roleNodeToDtoMapper = this.roleNodeMapperProvider.getToDtoMapper();
    return roleNodeToDtoMapper.toDto(jpaRoleNodes.get(0), mapContext);
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
    return roleNodeToDtoMapper.toDto(loadedParentRoleNode, mapContext);
  }


  @Override
  public User loadUserByToken(String token) {
    JpaUser user = this.userRepository.findByToken(token);
    if (user == null) {
      return null;
    }
    MappingContext mapContext = userMapperProvider.getMappingContextForRead();
    UserToDtoMapper userToDtoMapper = this.userMapperProvider.getToDtoMapper();
    return userToDtoMapper.toDto(user, mapContext);
  }


  @Override
  public User loadUserByUsername(String loginName) {
    JpaUser user = this.userRepository.findByLoginName(loginName);
    if (user == null) {
      return null;
    }
    MappingContext mapContext = userMapperProvider.getMappingContextForRead();
    UserToDtoMapper userToDtoMapper = this.userMapperProvider.getToDtoMapper();
    return userToDtoMapper.toDto(user, mapContext);
  }
}
