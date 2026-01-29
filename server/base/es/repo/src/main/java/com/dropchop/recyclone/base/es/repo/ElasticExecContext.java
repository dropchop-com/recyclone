package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.repo.ctx.BaseRepoExecContext;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;
import com.dropchop.recyclone.base.es.repo.config.ElasticIndexConfig;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@SuppressWarnings("UnusedReturnValue")
public class ElasticExecContext<E> extends BaseRepoExecContext<E> {

  IQueryObject query;
  ElasticIndexConfig indexConfig;

  public ElasticExecContext(@NonNull Class<E> rootClass, @NonNull String rootAlias) {
    super(rootClass, rootAlias);
  }

  public void init(ElasticIndexConfig indexConfig, IQueryObject query) {
    this.setIndexConfig(indexConfig);
    this.setQuery(query);
    super.bindListeners();
  }

  @Override // Overrides just for proper chaining.
  public ElasticExecContext<E> of(ExecContext<?> sourceContext) {
    super.of(sourceContext);
    return this;
  }

  public ElasticExecContext<E> decorateWith(ElasticCriteriaDecorator<E> decorator) {
    return (ElasticExecContext<E>) super.decorateWith(decorator);
  }
}
