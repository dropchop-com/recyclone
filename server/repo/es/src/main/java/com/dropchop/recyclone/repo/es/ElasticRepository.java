package com.dropchop.recyclone.repo.es;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.repo.api.CrudRepository;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
public abstract class ElasticRepository<E, ID> implements CrudRepository<E, ID> {

  public String getClassAlias(Class<?> cls) {
    return cls.getSimpleName().toLowerCase();
  }

  @Override
  public RepositoryExecContext<E> getRepositoryExecContext() {
    return null;
  }

  @Override
  public RepositoryExecContext<E> getRepositoryExecContext(MappingContext mappingContext) {
    return null;
  }

  @Override
  public <S extends E> List<S> save(Collection<S> entities) {
    return List.of();
  }

  @Override
  public <S extends E> S save(S entity) {
    return null;
  }

  @Override
  public <S extends E> void refresh(Collection<S> entities) {

  }

  @Override
  public int deleteById(Collection<? extends ID> ids) {
    return 0;
  }

  @Override
  public int deleteById(ID id) {
    return 0;
  }

  @Override
  public <S extends E> List<S> delete(Collection<S> entities) {
    return List.of();
  }

  @Override
  public <S extends E> S delete(S entity) {
    return null;
  }

  @Override
  public List<E> findById(Collection<ID> ids) {
    return List.of();
  }

  @Override
  public E findById(ID id) {
    return null;
  }

  @Override
  public List<E> find(RepositoryExecContext<E> context) {
    return List.of();
  }

  @Override
  public <X extends E> List<X> find(Class<X> cls, RepositoryExecContext<X> context) {
    return List.of();
  }

  @Override
  public List<E> find() {
    return List.of();
  }
}
