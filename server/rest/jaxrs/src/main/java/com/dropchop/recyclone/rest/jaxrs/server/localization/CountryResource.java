package com.dropchop.recyclone.rest.jaxrs.server.localization;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.service.api.ExecContextType;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.localization.CountryService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.dropchop.recyclone.model.api.rest.Constants.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 01. 22.
 */
@Slf4j
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class CountryResource implements
  com.dropchop.recyclone.rest.jaxrs.api.localization.CountryResource {

  @Inject
  CountryService service;

  @Inject
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  ParamsExecContextContainer ctxContainer;

  @Override
  public Result<Country> get() {
    return service.search();
  }

  @Override
  public List<Country> getRest() {
    return unwrap(get());
  }

  @Override
  public Result<Country> getByCode(String code) {
    Params params = ctxContainer.get().getParams();
    if (!(params instanceof CodeParams codeParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", CodeParams.class));
    }
    codeParams.setCodes(List.of(code));
    return service.search();
  }

  @Override
  public List<Country> getByCodeRest(String code) {
    return unwrap(getByCode(code));
  }

  @Override
  public Result<Country> search(CodeParams parameters) {
    return service.search();
  }

  @Override
  public List<Country> searchRest(CodeParams parameters) {
    return unwrap(search(parameters));
  }
}
