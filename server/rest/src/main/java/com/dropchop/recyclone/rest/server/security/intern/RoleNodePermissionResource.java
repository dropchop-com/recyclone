package com.dropchop.recyclone.rest.server.security.intern;

import com.dropchop.recyclone.model.dto.invoke.RoleNodePermissionParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.rest.server.ClassicRestByIdResource;
import com.dropchop.recyclone.service.api.security.RoleNodePermissionService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class RoleNodePermissionResource extends ClassicRestByIdResource <RoleNodePermission, RoleNodePermissionParams> implements
    com.dropchop.recyclone.rest.api.internal.security.RoleNodePermissionResource {

  @Inject
  RoleNodePermissionService service;

  @Inject
  RoleNodePermissionParams params;

  @Override
  public Result <RoleNodePermission> get() {
    return service.search();
  }

  @Override
  public Result <RoleNodePermission> getById(UUID id) {
    params.setIdentifiers(List.of(id.toString()));
    return service.search();
  }

  @Override
  public Result <RoleNodePermission> search(RoleNodePermissionParams params) {
    return service.search();
  }

  @Override
  public List <RoleNodePermission> searchRest(RoleNodePermissionParams params) {
    return unwrap(search(params));
  };

  @Override
  public Result <RoleNodePermission> create(List <RoleNodePermission> roles) {
    return service.create(roles);
  }

  @Override
  public Result <RoleNodePermission> delete(List <RoleNodePermission> roles) {
    return service.delete(roles);
  }

  @Override
  public Result <RoleNodePermission> update(List <RoleNodePermission> roles) {
    return service.update(roles);
  }
}
