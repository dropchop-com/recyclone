package com.dropchop.recyclone.quarkus.it.rest.server;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.quarkus.it.service.api.DummyService;
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
@Path(Paths.INTERNAL_SEGMENT + Constants.Paths.Test.DUMMY)
public class DummyResourceInternal implements
    com.dropchop.recyclone.quarkus.it.rest.api.DummyResourceInternal {

  @Inject
  ServiceSelector selector;

  @Override
  public Result<Dummy> create(List<Dummy> dummies) {
    return selector.select(DummyService.class).create(dummies);
  }

  @Override
  public Result<Dummy> delete(List<Dummy> dummies) {
    return selector.select(DummyService.class).delete(dummies);
  }

  @Override
  public Result<Dummy> update(List<Dummy> dummies) {
    return selector.select(DummyService.class).update(dummies);
  }
}
