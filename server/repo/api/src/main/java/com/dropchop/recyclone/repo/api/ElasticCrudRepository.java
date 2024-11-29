package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.model.api.invoke.ExecContextContainer;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.api.listener.QuerySearchResultListener;

import java.util.List;
import java.util.Map;

public interface ElasticCrudRepository<E, ID> extends CrudRepository<E, ID> {
  void setQuerySearchResultListener(QuerySearchResultListener querySearchResultListener);
  <S extends E> List<S> search(QueryParams params, RepositoryExecContext<S> execContext);
}
