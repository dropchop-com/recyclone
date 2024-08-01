package com.dropchop.recyclone.rest.server.query;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.query.Query;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.server.ClassicRestResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class QueryResource implements com.dropchop.recyclone.rest.api.query.QueryResource, ClassicRestResource<Query> {

  @Inject
  //QueryService service;

  @Override
  public Result<Query> getAll() {
    return null;
  }

  @Override
  public Result<Query> getById(String id) {
    return null;
  }

  @Override
  public Result<Query> create(Query query) {
    return null;
  }

  @Override
  public Result<Query> update(String id, Query query) {
    return null;
  }

  @Override
  public Result<Query> delete(String id) {
    return null;
  }

  @Override
  public Result<?> executeQuery(Query params) {
    return null;
  }
}
