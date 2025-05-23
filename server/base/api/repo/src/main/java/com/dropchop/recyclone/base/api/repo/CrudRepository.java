package com.dropchop.recyclone.base.api.repo;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.repo.ctx.RepositoryExecContext;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 02. 22.
 */
@SuppressWarnings("unused")
public interface CrudRepository<E extends Model, ID> extends ReadRepository<E, ID> {

  <S extends E> RepositoryExecContext<S> getRepositoryExecContext();
  <S extends E> RepositoryExecContext<S> getRepositoryExecContext(MappingContext mappingContext);

  <S extends E> List<S> save(Collection<S> entities);
  <S extends E> S save(S entity);

  <S extends E> void refresh(Collection<S> entities);

  <X extends ID> int deleteById(Collection<X> ids);
  <X extends ID> int deleteById(X id);

  <S extends E> List<S> delete(Collection<S> entities);
  <S extends E> S delete(S entity);
}
