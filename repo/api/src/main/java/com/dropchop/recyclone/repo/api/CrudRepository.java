package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
public interface CrudRepository<E, ID> extends Repository<E, ID> {

  <S extends E> List<S> save(Collection<S> entities);
  <S extends E> S save(S entity);

  List<E> findById(Collection<ID> ids);
  E findById(ID id);

  <P extends Params> List<E> find(RepositoryExecContext<E, P> context);
  List<E> find();

  <S extends E> void refresh(Collection<S> entities);

  int deleteById(Collection<? extends ID> ids);
  int deleteById(ID id);

  <S extends E> List<S> delete(Collection<S> entities);
  <S extends E> S delete(S entity);
}
