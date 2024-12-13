package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 02. 22.
 */
public interface ReadRepository<E extends Model, ID> extends Repository<E> {

  <S extends E, X extends ID> List<S> findById(Collection<X> ids);

  <S extends E, X extends ID> S findById(X id);

  <S extends E> List<S> find(RepositoryExecContext<S> context);

  <S extends E> List<S> find(Class<S> cls, RepositoryExecContext<S> context);

  <S extends E> List<S> find();
}
