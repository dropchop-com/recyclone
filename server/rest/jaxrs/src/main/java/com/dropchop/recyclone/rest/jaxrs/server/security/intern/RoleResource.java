package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.rest.jaxrs.ClassicRestByCodeResource;
import com.dropchop.recyclone.service.api.ExecContextType;
import com.dropchop.recyclone.service.api.security.RoleService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class RoleResource extends ClassicRestByCodeResource<Role, RoleParams> implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.security.RoleResource {

  @Inject
  RoleService service;

  @Inject
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  ParamsExecContextContainer ctxContainer;

  @Override
  public Result<Role> get() {
    return service.search();
  }

  @Override
  public Result<Role> getByCode(String code) {
    Params params = ctxContainer.get().getParams();
    if (!(params instanceof CodeParams codeParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", CodeParams.class));
    }
    codeParams.setCodes(List.of(code));
    return service.search();
  }

  @Override
  public Result<Role> search(RoleParams params) {
    return service.search();
  }

  @Override
  public Result<Role> create(List<Role> roles) {
    return service.create(roles);
  }

  @Override
  public Result<Role> delete(List<Role> roles) {
    return service.delete(roles);
  }

  @Override
  public Result<Role> update(List<Role> roles) {
    return service.update(roles);
  }

  @Override
  public Result<Role> addPermissions(RoleParams params) {
    return service.addPermissions(params);
  }

  @Override
  public List<Role> addPermissionsRest(RoleParams params) {
    return unwrap(addPermissions(params));
  }

  @Override
  public Result<Role> removePermissions(RoleParams params) {
    return service.removePermissions(params);
  }

  @Override
  public List<Role> removePermissionsRest(RoleParams params) {
    return unwrap(removePermissions(params));
  }
}
