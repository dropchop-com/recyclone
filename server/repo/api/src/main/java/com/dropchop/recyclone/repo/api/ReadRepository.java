package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
public interface ReadRepository<E, ID> extends Repository<E> {

  List<E> findById(Collection<ID> ids);

  E findById(ID id);

  List<E> find(RepositoryExecContext<E> context);

  //List<E> find(Class<? extends E> cls, RepositoryExecContext<E> context);
  <X extends E> List<X> find(Class<X> cls, RepositoryExecContext<X> context);

  List<E> find();
}
