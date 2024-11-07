package com.dropchop.recyclone.rest.server.security.intern;

import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.rest.server.ClassicRestByIdResource;
import com.dropchop.recyclone.service.api.security.RoleNodeService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class RoleNodeResource extends ClassicRestByIdResource<RoleNode, RoleNodeParams> implements
    com.dropchop.recyclone.rest.api.internal.security.RoleNodeResource {

  @Inject
  RoleNodeService service;

  @Inject
  RoleNodeParams params;

  @Override
  public Result<RoleNode> get() {
    return service.search();
  }

  @Override
  public Result<RoleNode> getById(UUID id) {
    params.setIdentifiers(List.of(id.toString()));
    return service.search();
  }

  @Override
  public Result<RoleNode> search(RoleNodeParams params) {
    return service.search();
  }

  @Override
  public List<RoleNode> searchRest(RoleNodeParams params) {
    return unwrap(search(params));
  };

  @Override
  public Result<RoleNode> create(List<RoleNode> roles) {
    return service.create(roles);
  }

  @Override
  public Result<RoleNode> delete(List<RoleNode> roles) {
    return service.delete(roles);
  }

  @Override
  public Result<RoleNode> update(List<RoleNode> roles) {
    return service.update(roles);
  }

  @Override
  public Result<RoleNode> addPermissions(RoleNodeParams params) {
    return service.addPermissions(params);
  }

  @Override
  public List<RoleNode> addPermissionsRest(RoleNodeParams params) {
    return unwrap(addPermissions(params));
  }

  @Override
  public Result<RoleNode> removePermissions(RoleNodeParams params) {
    return service.removePermissions(params);
  }

  @Override
  public List<RoleNode> removePermissionsRest(RoleNodeParams params) {
    return unwrap(removePermissions(params));
  }
}
