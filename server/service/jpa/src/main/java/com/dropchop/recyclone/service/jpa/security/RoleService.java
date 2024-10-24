package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.mapper.api.FilteringDtoContext;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.invoke.CommonExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ResultFilter;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.entity.jpa.security.JpaPermission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRole;
import com.dropchop.recyclone.repo.api.FilteringMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.PermissionRepository;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleRepository;
import com.dropchop.recyclone.service.api.JoinEntityHelper;
import com.dropchop.recyclone.service.api.RecycloneType;
import com.dropchop.recyclone.service.api.security.AuthorizationService;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
public class RoleService extends CrudServiceImpl<Role, JpaRole, String>
  implements com.dropchop.recyclone.service.api.security.RoleService {

  @Inject
  RoleMapperProvider mapperProvider;

  @Inject
  RoleRepository repository;

  @Inject
  PermissionRepository permissionRepository;

  @Inject
  CommonExecContextContainer execContextContainer;

  @Inject
  AuthorizationService authorizationService;

  @Override
  public RoleRepository getRepository() {
    return repository;
  }

  @Override
  public FilteringMapperProvider<Role, JpaRole, String> getMapperProvider() {
    return mapperProvider;
  }

  @Transactional
  public Result<Role> addPermissions(RoleParams params) {
    ResultFilter.ContentFilter contentFilter = params.tryGetResultContentFilter();
    if (contentFilter != null && contentFilter.getTreeLevel() < 4) {
      contentFilter.setTreeLevel(4);
    }
    MappingContext mapContext = new FilteringDtoContext().of(execContextContainer.get());
    Collection<JpaRole> roles = getRepository().find(repository.getRepositoryExecContext());
    JoinEntityHelper<JpaRole, JpaPermission, UUID> helper =
      new JoinEntityHelper<>(
          authorizationService, Constants.Domains.Security.PERMISSION, permissionRepository, roles
      );
    helper.join(
      toJoin -> params.getPermissionUuids(),
      helper.new ViewPermitter<>(execContextContainer.get()),
      (entity, join) -> entity.getPermissions().addAll(join)
    );
    save(roles);
    return getMapperProvider().getToDtoMapper().toDtosResult(roles, mapContext);
  }

  @Transactional
  public Result<Role> removePermissions(RoleParams params) {
    ResultFilter.ContentFilter contentFilter = params.tryGetResultContentFilter();
    if (contentFilter != null && contentFilter.getTreeLevel() < 4) {
      contentFilter.setTreeLevel(4);
    }
    MappingContext mapContext = new FilteringDtoContext().of(execContextContainer.get());
    Collection<JpaRole> roles = getRepository().find(getRepository().getRepositoryExecContext());
    JoinEntityHelper<JpaRole, JpaPermission, UUID> helper =
        new JoinEntityHelper<>(
            authorizationService, Constants.Domains.Security.PERMISSION, permissionRepository, roles
        );
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
    return getMapperProvider().getToDtoMapper().toDtosResult(roles, mapContext);
  }
}
