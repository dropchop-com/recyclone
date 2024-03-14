package com.dropchop.recyclone.quarkus.it.rest.server;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.Constants.Implementation;
import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.quarkus.it.service.api.DummyService;
import com.dropchop.recyclone.service.api.ExecContextType;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.quarkus.it.model.api.Constants;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@Path(Paths.PUBLIC_SEGMENT + Constants.Paths.Test.DUMMY)
public class DummyResource implements
    com.dropchop.recyclone.quarkus.it.rest.api.DummyResource {

  @Inject
  ServiceSelector selector;

  @Inject
  @ExecContextType(Implementation.RCYN_DEFAULT)
  ParamsExecContextContainer ctxContainer;

  @Override
  public Result<Dummy> getByCode(String code) {
    Params params = ctxContainer.get().getParams();
    if (!(params instanceof CodeParams codeParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", CodeParams.class));
    }
    codeParams.setCodes(List.of(code));
    return selector.select(DummyService.class).search();
  }

  @Override
  public Result<Dummy> get() {
    return selector.select(DummyService.class).search();
  }

  @Override
  public Result<Dummy> search(CodeParams params) {
    return selector.select(DummyService.class).search();
  }
}
