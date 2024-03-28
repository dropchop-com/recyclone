package com.dropchop.recyclone.rest.jaxrs.server.localization;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.service.api.ExecContextType;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.invoke.DefaultExecContextContainer;
import com.dropchop.recyclone.service.api.localization.LanguageService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 01. 22.
 */
@Slf4j
@RequestScoped
public class LanguageResource implements
  com.dropchop.recyclone.rest.jaxrs.api.localization.LanguageResource, ClassicRestResource<Language> {

  @Inject
  ServiceSelector selector;

  @Inject
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  DefaultExecContextContainer ctxContainer;

  @Override
  public Result<Language> get() {
    return selector.select(LanguageService.class).search();
  }

  @Override
  public List<Language> getRest() {
    return unwrap(get());
  }

  @Override
  public Result<Language> getByCode(String code) {
    Params params = ctxContainer.get().getParams();
    if (!(params instanceof CodeParams codeParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", CodeParams.class));
    }
    codeParams.setCodes(List.of(code));
    return selector.select(LanguageService.class).search();
  }

  @Override
  public List<Language> getByCodeRest(String code) {
    return unwrap(getByCode(code));
  }

  @Override
  public Result<Language> search(CodeParams parameters) {
    return selector.select(LanguageService.class).search();
  }

  @Override
  public List<Language> searchRest(CodeParams params) {
    return unwrap(search(params));
  }
}