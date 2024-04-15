package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.invoke.UserParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.rest.jaxrs.ClassicRestByIdResource;
import com.dropchop.recyclone.service.api.ExecContextType;
import com.dropchop.recyclone.service.api.security.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class UserResource extends ClassicRestByIdResource<User, UserParams> implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.security.UserResource {

  @Inject
  UserService service;

  @Inject
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  ParamsExecContextContainer ctxContainer;

  @Override
  public Result<User> get() {
    return null;
  }

  @Override
  public Result<User> search(UserParams params) {
    return service.search();
  }

  @Override
  public Result<User> getByUuid(UUID id) {
    Params params = ctxContainer.get().getParams();
    if (!(params instanceof IdentifierParams identifierParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", IdentifierParams.class));
    }
    identifierParams.setIdentifiers(List.of(id.toString()));
    return service.search();
  }

  @Override
  public User getByUuidRest(UUID id) {
    List<User> users = unwrap(getByUuid(id));
    if (users.isEmpty()) {
      return null;
    }
    return users.iterator().next();
  }

  @Override
  public Result<User> getById(UUID id) {
    return service.search();
  }

  @Override
  public Result<User> create(List<User> users) {
    return service.create(users);
  }

  @Override
  public Result<User> delete(List<User> data) {
    throw new ServiceException(ErrorCode.process_error, "Users can not be deleted!");
  }

  @Override
  public Result<User> update(List<User> users) {
    return service.update(users);
  }
}
