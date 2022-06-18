package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.invoke.CommonExecContext;
import com.dropchop.recyclone.service.api.security.RoleService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@Path(Paths.INTERNAL_SEGMENT + Paths.Security.ROLE)
public class RoleResource implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.security.RoleResource {

  @Inject
  ServiceSelector selector;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContext<Role> ctx;

  @Override
  public Result<Role> get() {
    return selector.select(RoleService.class).search();
  }

  @Override
  public Result<Role> getByCode(String code) {
    Params params = ctx.getParams();
    if (!(params instanceof CodeParams codeParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", CodeParams.class));
    }
    codeParams.setCodes(List.of(code));
    return selector.select(RoleService.class).search();
  }

  @Override
  public Result<Role> search(RoleParams params) {
    return selector.select(RoleService.class).search();
  }

  @Override
  public Result<Role> create(List<Role> roles) {
    return selector.select(RoleService.class).create(roles);
  }

  @Override
  public Result<Role> delete(List<Role> roles) {
    return selector.select(RoleService.class).delete(roles);
  }

  @Override
  public Result<Role> update(List<Role> roles) {
    return selector.select(RoleService.class).update(roles);
  }

  @Override
  public Result<Role> addPermissions(RoleParams params) {
    return selector.select(RoleService.class).addPermissions(params);
  }

  @Override
  public Result<Role> removePermissions(RoleParams params) {
    return selector.select(RoleService.class).removePermissions(params);
  }
}
