package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.repo.CrudRepository;
import com.dropchop.recyclone.base.api.repo.ctx.RepositoryExecContext;

public interface ElasticCrudRepository<E extends Model, ID> extends CrudRepository<E, ID> {
  <S extends E> int deleteByQuery(RepositoryExecContext<S> execContext);
}
