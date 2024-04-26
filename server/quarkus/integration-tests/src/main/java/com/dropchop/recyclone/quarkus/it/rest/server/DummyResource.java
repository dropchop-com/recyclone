package com.dropchop.recyclone.quarkus.it.rest.server;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.invoke.DefaultExecContext;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.service.api.DummyService;
import com.dropchop.recyclone.rest.jaxrs.ClassicReadByCodeResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class DummyResource extends ClassicReadByCodeResource<Dummy, CodeParams> implements
    com.dropchop.recyclone.quarkus.it.rest.api.DummyResource {

  @Inject
  DummyService service;

  @Inject
  CodeParams codeParams;

  @Inject
  DefaultExecContext<Dummy> execContext;

  @Override
  public Result<Dummy> getByCode(String code) {
    codeParams.setCodes(List.of(code));
    return service.search();
  }

  @Override
  public Result<Dummy> get() {
    Params params = execContext.getParams();
    List<String> codes = codeParams.getCodes();
    return service.search();
  }

  @Override
  public Result<Dummy> search(CodeParams params) {
    return service.search();
  }
}
