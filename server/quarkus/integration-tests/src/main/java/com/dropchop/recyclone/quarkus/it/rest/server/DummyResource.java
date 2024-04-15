package com.dropchop.recyclone.quarkus.it.rest.server;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.Constants.Implementation;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.service.api.DummyService;
import com.dropchop.recyclone.rest.jaxrs.ClassicRestResource;
import com.dropchop.recyclone.service.api.ExecContextType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
public class DummyResource implements
    ClassicRestResource<Dummy>,
    com.dropchop.recyclone.quarkus.it.rest.api.DummyResource {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  DummyService service;

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
    return service.search();
  }

  @Override
  public List<Dummy> getByCodeRest(String code) {
    return unwrap(getByCode(code));
  }

  @Override
  public Result<Dummy> get() {
    return service.search();
  }

  @Override
  public List<Dummy> getRest() {
    return unwrap(get());
  }

  @Override
  public Result<Dummy> search(CodeParams params) {
    return service.search();
  }

  @Override
  public List<Dummy> searchRest(CodeParams params) {
    return unwrap(search(params));
  }
}
