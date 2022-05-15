package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.security.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@Path(Paths.INTERNAL + Paths.Security.ROLE)
@RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Constants.Actions.VIEW)
public class RoleResource implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.security.RoleResource {

  @Inject
  ServiceSelector selector;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContext<CodeParams, Role> ctx;

  @Override
  public Result<Role> get() {
    return selector.select(RoleService.class).search();
  }

  @Override
  public Result<Role> getByCode(String code) {
    CodeParams params = ctx.getParams();
    params.setCodes(List.of(code));
    return selector.select(RoleService.class).search();
  }

  @Override
  public Result<Role> search(CodeParams params) {
    return selector.select(RoleService.class).search();
  }

  @Override
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Constants.Actions.CREATE)
  public Result<Role> create(List<Role> roles) {
    return selector.select(RoleService.class).create(roles);
  }

  @Override
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Constants.Actions.DELETE)
  public Result<Role> delete(List<Role> roles) {
    return selector.select(RoleService.class).delete(roles);
  }

  @Override
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Constants.Actions.UPDATE)
  public Result<Role> update(List<Role> roles) {
    return selector.select(RoleService.class).update(roles);
  }
}
