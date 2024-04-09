package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.invoke.*;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.service.api.ExecContextType;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.security.DomainService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
//@Path(Paths.INTERNAL_SEGMENT + Paths.Security.DOMAIN)
public class DomainResource implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.security.DomainResource {

  @Inject
  ServiceSelector selector;

  @Inject
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  ParamsExecContextContainer ctxContainer;

  @Override
  public Result<Domain> get() {
    return selector.select(DomainService.class).search();
  }

  @Override
  public Result<Domain> getByCode(String code) {
    Params params = ctxContainer.get().getParams();
    if (!(params instanceof CodeParams codeParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", CodeParams.class));
    }
    codeParams.setCodes(List.of(code));
    return selector.select(DomainService.class).search();
  }

  @Override
  public Result<Domain> search(CodeParams params) {
    return selector.select(DomainService.class).search();
  }

  @Override
  public Result<Domain> create(List<Domain> domains) {
    return selector.select(DomainService.class).create(domains);
  }

  @Override
  public Result<Domain> delete(List<Domain> domains) {
    return selector.select(DomainService.class).delete(domains);
  }

  @Override
  public Result<Domain> update(List<Domain> domains) {
    return selector.select(DomainService.class).update(domains);
  }
}
