package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@SuppressWarnings("unused")
public interface CrudRepository<E, ID> extends ReadRepository<E, ID> {

  RepositoryExecContext<E> getRepositoryExecContext();

  <S extends E> List<S> save(Collection<S> entities);
  <S extends E> S save(S entity);

  <S extends E> void refresh(Collection<S> entities);

  int deleteById(Collection<? extends ID> ids);
  int deleteById(ID id);

  <S extends E> List<S> delete(Collection<S> entities);
  <S extends E> S delete(S entity);
}
