package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;

import java.util.List;

public interface ElasticCrudRepository<E extends Model, ID> extends CrudRepository<E, ID> {
  <S extends E> List<S> search(QueryParams params, RepositoryExecContext<S> execContext);
  <S extends E> int deleteByQuery(RepositoryExecContext<S> execContext);
}
