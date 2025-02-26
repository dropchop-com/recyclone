package com.dropchop.recyclone.base.api.repo;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.repo.ctx.RepositoryExecContext;
import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;

import java.util.List;

public interface ElasticCrudRepository<E extends Model, ID> extends CrudRepository<E, ID> {
  <S extends E> List<S> search(QueryParams params, RepositoryExecContext<S> execContext);

  <S extends E> int deleteByQuery(RepositoryExecContext<S> execContext);

  List<String> setDatesForIndices(QueryNodeObject queryNodeObject);
}
