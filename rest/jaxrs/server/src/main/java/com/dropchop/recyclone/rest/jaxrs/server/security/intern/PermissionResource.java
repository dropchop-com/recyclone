package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.rest.jaxrs.ServiceSelector;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.security.PermissionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@Path(Paths.INTERNAL + Paths.Security.PERMISSION)
@RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Constants.Actions.VIEW)
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
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Constants.Actions.CREATE)
  public Result<Permission> create(List<Permission> roles) {
    return selector.select(PermissionService.class).create(roles);
  }

  @Override
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Constants.Actions.DELETE)
  public Result<Permission> delete(List<Permission> roles) {
    return selector.select(PermissionService.class).delete(roles);
  }

  @Override
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Constants.Actions.UPDATE)
  public Result<Permission> update(List<Permission> roles) {
    return selector.select(PermissionService.class).update(roles);
  }
}
