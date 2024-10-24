package com.dropchop.recyclone.rest.server.security.intern;

import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.rest.server.ClassicRestByIdResource;
import com.dropchop.recyclone.service.api.security.PermissionService;
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
    com.dropchop.recyclone.rest.api.internal.security.PermissionResource {

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
