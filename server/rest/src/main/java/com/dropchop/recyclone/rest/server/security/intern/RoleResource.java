package com.dropchop.recyclone.rest.server.security.intern;

import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.rest.server.ClassicRestByCodeResource;
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
    com.dropchop.recyclone.rest.api.internal.security.RoleResource {

  @Inject
  RoleService service;

  @Inject
  RoleParams params;

  @Override
  public Result<Role> get() {
    return service.search();
  }

  @Override
  public Result<Role> getByCode(String code) {
    params.setCodes(List.of(code));
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