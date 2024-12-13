package com.dropchop.recyclone.base.jaxrs.security.internal;

import com.dropchop.recyclone.base.dto.model.invoke.RoleNodePermissionParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.rest.server.ClassicRestByIdResource;
import com.dropchop.recyclone.base.api.service.security.RoleNodePermissionService;
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
    com.dropchop.recyclone.base.api.jaxrs.internal.security.RoleNodePermissionResource<RoleNodePermission> {

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
  }

  @Override
  public Result <RoleNodePermission> create(List <RoleNodePermission> permissions) {
    return service.create(permissions);
  }

  @Override
  public Result <RoleNodePermission> delete(List <RoleNodePermission> permissions) {
    return service.delete(permissions);
  }

  @Override
  public Result <RoleNodePermission> update(List <RoleNodePermission> permissions) {
    return service.update(permissions);
  }
}
