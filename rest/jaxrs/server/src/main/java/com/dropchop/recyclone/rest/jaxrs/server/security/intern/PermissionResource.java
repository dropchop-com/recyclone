package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.service.api.invoke.CommonExecContext;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.security.PermissionService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@Path(Paths.INTERNAL_SEGMENT + Paths.Security.PERMISSION)
public class PermissionResource implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.security.PermissionResource {

  @Inject
  ServiceSelector selector;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContext<IdentifierParams, Role> ctx;

  @Override
  public Result<Permission> get() {
    return selector.select(PermissionService.class).search();
  }

  @Override
  public Result<Permission> getById(UUID id) {
    IdentifierParams params = ctx.getParams();
    params.setIdentifiers(List.of(id.toString()));
    return selector.select(PermissionService.class).search();
  }

  @Override
  public Result<Permission> search(IdentifierParams params) {
    return selector.select(PermissionService.class).search();
  }

  @Override
  public Result<Permission> create(List<Permission> roles) {
    return selector.select(PermissionService.class).create(roles);
  }

  @Override
  public Result<Permission> delete(List<Permission> roles) {
    return selector.select(PermissionService.class).delete(roles);
  }

  @Override
  public Result<Permission> update(List<Permission> roles) {
    return selector.select(PermissionService.class).update(roles);
  }
}
