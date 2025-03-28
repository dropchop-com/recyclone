package com.dropchop.recyclone.quarkus.it.rest.server;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.service.api.DummyService;
import com.dropchop.recyclone.base.api.rest.ClassicModifyResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_ES_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@RequestScoped
public class DummyResourceInternal extends ClassicModifyResource<Dummy> implements
    com.dropchop.recyclone.quarkus.it.rest.api.DummyResourceInternal {

  @Inject
  @RecycloneType(RECYCLONE_ES_DEFAULT)
  DummyService service;

  @Override
  public Result<Dummy> create(List<Dummy> dummies) {
    return service.create(dummies);
  }

  @Override
  public Result<Dummy> delete(List<Dummy> dummies) {
    return service.delete(dummies);
  }

  @Override
  public Result<Dummy> update(List<Dummy> dummies) {
    return service.update(dummies);
  }

  @Override
  public int deleteById(CodeParams codeParams) {
    return service.delete();
  }

  @Override
  public int deleteByQuery(QueryParams codeParams) {
    return service.deleteByQuery();
  }
}
