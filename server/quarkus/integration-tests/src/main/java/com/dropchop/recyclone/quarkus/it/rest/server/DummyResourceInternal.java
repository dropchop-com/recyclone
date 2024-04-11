package com.dropchop.recyclone.quarkus.it.rest.server;

import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.service.api.DummyService;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.service.api.ServiceSelector;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
//@Path(Constants.Paths.INTERNAL_SEGMENT + DUMMY)
public class DummyResourceInternal implements
    ClassicRestResource<Dummy>,
    com.dropchop.recyclone.quarkus.it.rest.api.DummyResourceInternal {

  @Inject
  ServiceSelector selector;

  @Override
  public Result<Dummy> create(List<Dummy> dummies) {
    return selector.select(DummyService.class).create(dummies);
  }

  @Override
  public List<Dummy> createRest(List<Dummy> data) {
    return unwrap(create(data));
  }

  @Override
  public Result<Dummy> delete(List<Dummy> dummies) {
    return selector.select(DummyService.class).delete(dummies);
  }

  @Override
  public List<Dummy> deleteRest(List<Dummy> data) {
    return unwrap(delete(data));
  }

  @Override
  public Result<Dummy> update(List<Dummy> dummies) {
    return selector.select(DummyService.class).update(dummies);
  }

  @Override
  public List<Dummy> updateRest(List<Dummy> data) {
    return unwrap(update(data));
  }
}
