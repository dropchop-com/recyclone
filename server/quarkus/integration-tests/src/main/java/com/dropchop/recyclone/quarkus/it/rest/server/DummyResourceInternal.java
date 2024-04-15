package com.dropchop.recyclone.quarkus.it.rest.server;

import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.service.api.DummyService;
import com.dropchop.recyclone.rest.jaxrs.ClassicRestResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class DummyResourceInternal implements
    ClassicRestResource<Dummy>,
    com.dropchop.recyclone.quarkus.it.rest.api.DummyResourceInternal {

  @Inject
  DummyService service;

  @Override
  public Result<Dummy> create(List<Dummy> dummies) {
    return service.create(dummies);
  }

  @Override
  public List<Dummy> createRest(List<Dummy> data) {
    return unwrap(create(data));
  }

  @Override
  public Result<Dummy> delete(List<Dummy> dummies) {
    return service.delete(dummies);
  }

  @Override
  public List<Dummy> deleteRest(List<Dummy> data) {
    return unwrap(delete(data));
  }

  @Override
  public Result<Dummy> update(List<Dummy> dummies) {
    return service.update(dummies);
  }

  @Override
  public List<Dummy> updateRest(List<Dummy> data) {
    return unwrap(update(data));
  }
}
