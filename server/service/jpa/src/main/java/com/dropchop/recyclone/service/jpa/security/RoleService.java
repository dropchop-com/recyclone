package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.invoke.CommonExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ResultFilter;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.entity.jpa.security.JpaPermission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRole;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleRepository;
import com.dropchop.recyclone.service.api.JoinEntityHelper;
import com.dropchop.recyclone.service.api.ServiceConfiguration;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.api.invoke.FilteringDtoContext;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.security.AuthorizationService;
import com.dropchop.recyclone.service.jpa.RecycloneCrudServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@ServiceType(RECYCLONE_JPA_DEFAULT)
public class RoleService extends RecycloneCrudServiceImpl<Role, JpaRole, String>
  implements com.dropchop.recyclone.service.api.security.RoleService {

  @Inject
  @RepositoryType(RECYCLONE_JPA_DEFAULT)
  RoleRepository repository;

  @Inject
  RoleToDtoMapper toDtoMapper;

  @Inject
  RoleToEntityMapper toEntityMapper;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContextContainer execContextContainer;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  AuthorizationService authorizationService;

  @Inject
  @ServiceType(RECYCLONE_JPA_DEFAULT)
  PermissionService permissionService;

  @Override
  public ServiceConfiguration<Role, JpaRole, String> getConfiguration() {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper
    );
  }

  @Transactional
  public Result<Role> addPermissions(RoleParams params) {
    ResultFilter.ContentFilter contentFilter = params.tryGetResultContentFilter();
    if (contentFilter != null && contentFilter.getTreeLevel() < 4) {
      contentFilter.setTreeLevel(4);
    }
    MappingContext mapContext = new FilteringDtoContext().of(execContextContainer.get());
    Collection<JpaRole> roles = find(getRepositoryExecContext());
    JoinEntityHelper<JpaRole, JpaPermission, UUID> helper =
      new JoinEntityHelper<>(authorizationService, permissionService, roles);
    helper.join(
      toJoin -> params.getPermissionUuids(),
      helper.new ViewPermitter<>(execContextContainer.get()),
      (entity, join) -> entity.getPermissions().addAll(join)
    );
    save(roles);
    return toDtoMapper.toDtosResult(roles, mapContext);
  }

  @Transactional
  public Result<Role> removePermissions(RoleParams params) {
    ResultFilter.ContentFilter contentFilter = params.tryGetResultContentFilter();
    if (contentFilter != null && contentFilter.getTreeLevel() < 4) {
      contentFilter.setTreeLevel(4);
    }
    MappingContext mapContext = new FilteringDtoContext().of(execContextContainer.get());
    Collection<JpaRole> roles = find(getRepositoryExecContext());
    JoinEntityHelper<JpaRole, JpaPermission, UUID> helper =
      new JoinEntityHelper<>(authorizationService, permissionService, roles);
    helper.join(
      toJoin -> params.getPermissionUuids(),
      helper.new ViewPermitter<>(execContextContainer.get()),
      (entity, join) -> {
        Set<JpaPermission> permissions = entity.getPermissions();
        for (JpaPermission permission : join) {
          if (!permissions.remove(permission)) {
            throw new ServiceException(ErrorCode.data_validation_error, "Missing permission for role!",
              Set.of(
                new AttributeString(entity.identifierField(), entity.identifier()),
                new AttributeString(permission.identifierField(), permission.identifier())
              )
            );
          }
        }
      }
    );
    save(roles);
    return toDtoMapper.toDtosResult(roles, mapContext);
  }
}
