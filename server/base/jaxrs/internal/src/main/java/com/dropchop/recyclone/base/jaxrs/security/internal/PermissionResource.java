package com.dropchop.recyclone.base.jaxrs.security.internal;

import com.dropchop.recyclone.base.dto.model.invoke.IdentifierParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.Permission;
import com.dropchop.recyclone.base.api.rest.ClassicRestByIdResource;
import com.dropchop.recyclone.base.api.service.security.PermissionService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class PermissionResource extends ClassicRestByIdResource<Permission, IdentifierParams> implements
    com.dropchop.recyclone.base.api.jaxrs.internal.security.PermissionResource {

  @Inject
  PermissionService service;

  @Inject
  IdentifierParams params;

  @Override
  public Result<Permission> get() {
    return service.search();
  }

  @Override
  public Result<Permission> getById(UUID id) {
    params.setIdentifiers(List.of(id.toString()));
    return service.search();
  }

  @Override
  public Result<Permission> search(IdentifierParams params) {
    return service.search();
  }

  @Override
  public Result<Permission> create(List<Permission> permissions) {
    return service.create(permissions);
  }

  @Override
  public Result<Permission> delete(List<Permission> permissions) {
    return service.delete(permissions);
  }

  @Override
  public Result<Permission> update(List<Permission> permissions) {
    return service.update(permissions);
  }
}
