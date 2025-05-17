package com.dropchop.recyclone.base.jaxrs.security.internal;

import com.dropchop.recyclone.base.api.rest.ClassicRestByIdResource;
import com.dropchop.recyclone.base.api.service.security.UserAccountService;
import com.dropchop.recyclone.base.dto.model.invoke.UserAccountParams;
import com.dropchop.recyclone.base.dto.model.invoke.UserAccountParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class UserAccountResource extends ClassicRestByIdResource <UserAccount, UserAccountParams> implements
    com.dropchop.recyclone.base.api.jaxrs.internal.security.UserAccountResource<UserAccount> {

  @Inject
  UserAccountService service;

  @Inject
  UserAccountParams params;

  @Override
  public Result <UserAccount> get() {
    return service.search();
  }

  @Override
  public Result <UserAccount> getById(UUID id) {
    params.setIdentifiers(List.of(id.toString()));
    return service.search();
  }

  @Override
  public Result <UserAccount> search(UserAccountParams params) {
    return service.search();
  }

  @Override
  public List <UserAccount> searchRest(UserAccountParams params) {
    return unwrap(search(params));
  }

  @Override
  public Result <UserAccount> create(List <UserAccount> accounts) {
    return service.create(accounts);
  }

  @Override
  public Result <UserAccount> delete(List <UserAccount> accounts) {
    return service.delete(accounts);
  }

  @Override
  public Result <UserAccount> update(List <UserAccount> accounts) {
    return service.update(accounts);
  }
}
