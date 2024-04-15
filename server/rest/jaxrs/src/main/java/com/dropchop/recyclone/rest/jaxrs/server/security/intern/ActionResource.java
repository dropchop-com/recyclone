package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.rest.jaxrs.ClassicRestByCodeResource;
import com.dropchop.recyclone.service.api.ExecContextType;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.security.ActionService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
public class ActionResource extends ClassicRestByCodeResource<Action, CodeParams> implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.security.ActionResource {

  @Inject
  ServiceSelector selector;

  @Inject
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  ParamsExecContextContainer ctxContainer;

  @Override
  public Result<Action> getByCode(String code) {
    Params params = ctxContainer.get().getParams();
    if (!(params instanceof CodeParams codeParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", CodeParams.class));
    }
    codeParams.setCodes(List.of(code));
    return selector.select(ActionService.class).search();
  }

  @Override
  public Result<Action> get() {
    return selector.select(ActionService.class).search();
  }

  @Override
  public Result<Action> search(CodeParams params) {
    return selector.select(ActionService.class).search();
  }

  @Override
  public Result<Action> create(List<Action> actions) {
    return selector.select(ActionService.class).create(actions);
  }

  @Override
  public Result<Action> delete(List<Action> actions) {
    return selector.select(ActionService.class).delete(actions);
  }

  @Override
  public Result<Action> update(List<Action> actions) {
    return selector.select(ActionService.class).update(actions);
  }
}
