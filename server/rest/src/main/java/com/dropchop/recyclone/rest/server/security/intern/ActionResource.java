package com.dropchop.recyclone.rest.server.security.intern;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.rest.server.ClassicRestByCodeResource;
import com.dropchop.recyclone.service.api.security.ActionService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class ActionResource extends ClassicRestByCodeResource<Action, CodeParams> implements
    com.dropchop.recyclone.rest.api.internal.security.ActionResource {

  @Inject
  ActionService service;

  @Inject
  CodeParams params;

  @Override
  public Result<Action> getByCode(String code) {
    params.setCodes(List.of(code));
    return service.search();
  }

  @Override
  public Result<Action> get() {
    return service.search();
  }

  @Override
  public Result<Action> search(CodeParams params) {
    return service.search();
  }

  @Override
  public Result<Action> create(List<Action> actions) {
    return service.create(actions);
  }

  @Override
  public Result<Action> delete(List<Action> actions) {
    return service.delete(actions);
  }

  @Override
  public Result<Action> update(List<Action> actions) {
    return service.update(actions);
  }
}
