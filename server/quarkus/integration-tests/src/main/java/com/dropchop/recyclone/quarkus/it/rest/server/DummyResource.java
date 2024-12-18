package com.dropchop.recyclone.quarkus.it.rest.server;

import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.model.dto.invoke.DummyQueryParams;
import com.dropchop.recyclone.quarkus.it.service.api.DummyService;
import com.dropchop.recyclone.base.api.rest.ClassicReadByCodeResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class DummyResource extends ClassicReadByCodeResource<Dummy, CodeParams> implements
    com.dropchop.recyclone.quarkus.it.rest.api.DummyResource {

  @Inject
  DummyService service;

  @Inject
  CodeParams codeParams;

  @Override
  public Result<Dummy> getByCode(String code) {
    codeParams.setCodes(List.of(code));
    return service.search();
  }

  @Override
  public Result<Dummy> get() {
    return service.search();
  }

  @Override
  public Result<Dummy> search(CodeParams params) {
    return service.search();
  }

  @Override
  public Result<Dummy> query(DummyQueryParams params) {
    return service.query();
  }

  @Override
  public List<Dummy> queryRest(DummyQueryParams params) {
    return service.query().getData();
  }
}
