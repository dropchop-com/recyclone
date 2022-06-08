package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.entity.jpa.security.EPermission;
import com.dropchop.recyclone.model.entity.jpa.security.ERole;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleRepository;
import com.dropchop.recyclone.service.api.invoke.CommonExecContext;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.api.invoke.FilteringDtoContext;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.JoinEntityHelper;
import com.dropchop.recyclone.service.jpa.blaze.CrudServiceImpl;
import com.dropchop.recyclone.service.jpa.blaze.ServiceConfiguration;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@ServiceType(RCYN_DEFAULT)
public class RoleService extends CrudServiceImpl<Role, RoleParams, ERole, String>
  implements com.dropchop.recyclone.service.api.security.RoleService {

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  RoleRepository repository;

  @Inject
  RoleToDtoMapper toDtoMapper;

  @Inject
  RoleToEntityMapper toEntityMapper;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContext<RoleParams, Role> ctx;

  @Inject
  @ServiceType(RCYN_DEFAULT)
  PermissionService permissionService;

  @Override
  public ServiceConfiguration<Role, RoleParams, ERole, String> getConfiguration() {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper
    );
  }

  @Transactional
  public Result<Role> addPermissions(RoleParams params) {
    MappingContext<RoleParams> mapContext = new FilteringDtoContext<RoleParams>().of(ctx);
    JoinEntityHelper<ERole, Permission, EPermission, UUID> helper = getJoinHelper(permissionService);
    List<ERole> roles = helper.apply(params::getPermissionUuids, ctx, (entity, bound) -> {
      entity.getPermissions().addAll(bound);
      repository.save(entity);
    });
    ServiceConfiguration<Role, RoleParams, ERole, String> conf = getConfiguration();
    return conf.getToDtoMapper().toDtosResult(roles, mapContext);
  }

  @Transactional
  public Result<Role> removePermissions(RoleParams params) {

    MappingContext<RoleParams> mapContext = new FilteringDtoContext<RoleParams>().of(ctx);
    JoinEntityHelper<ERole, Permission, EPermission, UUID> helper = getJoinHelper(permissionService);
    List<ERole> roles = helper.apply(params::getPermissionUuids, ctx, (entity, bound) -> {
      SortedSet<EPermission> permissions = entity.getPermissions();
      for (EPermission permission : bound) {
        if (!permissions.remove(permission)) {
          throw new ServiceException(ErrorCode.authorization_error, "Missing permission for role!",
            Set.of(
              new AttributeString(entity.identifierField(), entity.identifier()),
              new AttributeString(permission.identifierField(), permission.identifier())
            )
          );
        }
      }
      repository.save(entity);
    });
    ServiceConfiguration<Role, RoleParams, ERole, String> conf = getConfiguration();
    return conf.getToDtoMapper().toDtosResult(roles, mapContext);
  }
}
