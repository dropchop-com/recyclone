package com.dropchop.recyclone.base.jaxrs.security.internal;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.dto.model.invoke.UserParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.api.rest.ClassicRestByIdResource;
import com.dropchop.recyclone.base.api.service.security.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class UserResource extends ClassicRestByIdResource<User, UserParams> implements
    com.dropchop.recyclone.base.api.jaxrs.internal.security.UserResource {

  @Inject
  UserService service;

  @Inject
  UserParams params;

  @Override
  public Result<User> get() {
    return null;
  }

  @Override
  public Result<User> getById(UUID id) {
    params.setIdentifiers(List.of(id.toString()));
    return service.search();
  }

  @Override
  public Result<User> search(UserParams params) {
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
