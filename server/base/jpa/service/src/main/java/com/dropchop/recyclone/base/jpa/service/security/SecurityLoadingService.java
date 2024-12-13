package com.dropchop.recyclone.base.jpa.service.security;

import com.dropchop.recyclone.base.api.mapper.FilteringDtoContext;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.jpa.mapper.security.RoleNodeToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.UserToDtoMapper;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.dto.model.invoke.Params;
import com.dropchop.recyclone.base.dto.model.invoke.RoleNodeParams;
import com.dropchop.recyclone.base.dto.model.security.RoleNode;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.jpa.repo.security.RoleNodeMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.security.RoleNodeRepository;
import com.dropchop.recyclone.base.jpa.repo.security.UserMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.security.UserRepository;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNode;
import com.dropchop.recyclone.base.jpa.model.security.JpaUser;
import com.dropchop.recyclone.base.api.repo.ctx.RepositoryExecContext;
import com.dropchop.recyclone.base.api.service.RecycloneType;
import com.dropchop.recyclone.base.api.service.security.HierarchicalSecurityLoadingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

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
  UserMapperProvider userMapperProvider;

  @Inject
  RoleNodeRepository roleNodeRepository;

  @Inject
  RoleNodeMapperProvider roleNodeMapperProvider;


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
    return roleNodeToDtoMapper.toDto(jpaRoleNodes.getFirst(), mapContext);
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
    return this.mapToUser(this.userRepository.findByToken(token));
  }


  @Override
  public User loadUserByUsername(String loginName) {
    return this.mapToUser(this.userRepository.findByLoginName(loginName));
  }
}
