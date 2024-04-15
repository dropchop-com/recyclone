package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.rest.jaxrs.ClassicRestByIdResource;
import com.dropchop.recyclone.service.api.ExecContextType;
import com.dropchop.recyclone.service.api.security.PermissionService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class PermissionResource extends ClassicRestByIdResource<Permission, IdentifierParams> implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.security.PermissionResource {

  @Inject
  PermissionService service;

  @Inject
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  ParamsExecContextContainer ctxContainer;

  @Override
  public Result<Permission> get() {
    return service.search();
  }

  @Override
  public Result<Permission> getById(UUID id) {
    Params params = ctxContainer.get().getParams();
    if (!(params instanceof IdentifierParams identifierParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", IdentifierParams.class));
    }
    identifierParams.setIdentifiers(List.of(id.toString()));
    return service.search();
  }

  @Override
  public Result<Permission> search(IdentifierParams params) {
    return service.search();
  }

  @Override
  public Result<Permission> create(List<Permission> roles) {
    return service.create(roles);
  }

  @Override
  public Result<Permission> delete(List<Permission> roles) {
    return service.delete(roles);
  }

  @Override
  public Result<Permission> update(List<Permission> roles) {
    return service.update(roles);
  }
}
