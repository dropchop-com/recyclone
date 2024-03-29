package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ResultFilter;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.entity.jpa.security.EPermission;
import com.dropchop.recyclone.model.entity.jpa.security.ERole;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleRepository;
import com.dropchop.recyclone.service.api.ExecContextType;
import com.dropchop.recyclone.service.api.JoinEntityHelper;
import com.dropchop.recyclone.service.api.ServiceConfiguration;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.api.invoke.DefaultExecContextContainer;
import com.dropchop.recyclone.service.api.invoke.FilteringDtoContext;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.security.AuthorizationService;
import com.dropchop.recyclone.service.jpa.blaze.RecycloneCrudServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@ServiceType(RCYN_DEFAULT)
public class RoleService extends RecycloneCrudServiceImpl<Role, ERole, String>
  implements com.dropchop.recyclone.service.api.security.RoleService {

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  RoleRepository repository;

  @Inject
  RoleToDtoMapper toDtoMapper;

  @Inject
  RoleToEntityMapper toEntityMapper;

  @Inject
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  DefaultExecContextContainer ctxContainer;


  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  AuthorizationService authorizationService;

  @Inject
  @ServiceType(RCYN_DEFAULT)
  PermissionService permissionService;

  @Override
  public ServiceConfiguration<Role, ERole, String> getConfiguration() {
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
    MappingContext mapContext = new FilteringDtoContext().of(ctxContainer.get());
    Collection<ERole> roles = find(getRepositoryExecContext());
    JoinEntityHelper<ERole, EPermission, UUID> helper =
      new JoinEntityHelper<>(authorizationService, permissionService, roles);
    helper.join(
      toJoin -> params.getPermissionUuids(),
      helper.new ViewPermitter<>(ctxContainer.get()),
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
    MappingContext mapContext = new FilteringDtoContext().of(ctxContainer.get());
    Collection<ERole> roles = find(getRepositoryExecContext());
    JoinEntityHelper<ERole, EPermission, UUID> helper =
      new JoinEntityHelper<>(authorizationService, permissionService, roles);
    helper.join(
      toJoin -> params.getPermissionUuids(),
      helper.new ViewPermitter<>(ctxContainer.get()),
      (entity, join) -> {
        Set<EPermission> permissions = entity.getPermissions();
        for (EPermission permission : join) {
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
