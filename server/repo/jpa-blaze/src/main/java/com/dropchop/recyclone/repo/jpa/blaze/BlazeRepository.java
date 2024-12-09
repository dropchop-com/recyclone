package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.DeleteCriteriaBuilder;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.api.RepositoryExecContextListener;
import com.dropchop.recyclone.mapper.api.TotalCountExecContextListener;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.ExecContextContainer;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import com.dropchop.recyclone.repo.api.CrudRepository;
import com.dropchop.recyclone.repo.api.ctx.*;
import lombok.extern.slf4j.Slf4j;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 02. 22.
 */
@Slf4j
public abstract class BlazeRepository<E, ID> implements CrudRepository<E, ID> {

  @Inject
  EntityManager em;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CriteriaBuilderFactory cbf;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  ExecContextContainer ctxContainer;

  public String getClassAlias(Class<?> cls) {
    return cls.getSimpleName().toLowerCase();
  }

  protected Collection<CriteriaDecorator> getCommonCriteriaDecorators() {
    return List.of(
        new LikeIdentifiersCriteriaDecorator(),
        new InlinedStatesCriteriaDecorator(),
        new SortCriteriaDecorator(),
        new PageCriteriaDecorator()
    );
  }

  @Override
  public RepositoryExecContext<E> getRepositoryExecContext() {
    BlazeExecContext<E> context = new BlazeExecContext<E>().of(ctxContainer.get());
    for (CriteriaDecorator decorator : getCommonCriteriaDecorators()) {
      context.decorateWith(decorator);
    }
    return context;
  }

  @Override
  public RepositoryExecContext<E> getRepositoryExecContext(MappingContext mappingContext) {
    return getRepositoryExecContext().totalCount(mappingContext);
  }

  public <X extends E> CriteriaBuilder<X> getBuilder(Class<X> cls) {
    return cbf.create(em, cls);
  }

  @Override
  public E findById(ID id) {
    List<E> entities = findById(List.of(id));
    return entities.isEmpty() ? null : entities.getFirst();
  }

  @Override
  public List<E> findById(Collection<ID> ids) {
    Class<E> tClass = getRootClass();
    String alias = getClassAlias(tClass);
    CriteriaBuilder<E> cb = getBuilder(tClass).from(getRootClass(), alias);
    if (HasCode.class.isAssignableFrom(tClass)) {
      cb.where(alias + ".code").in(ids);
    } else if (HasUuid.class.isAssignableFrom(tClass)) {
      cb.where(alias + ".uuid").in(ids);
    }
    return cb.getResultList();
  }

  @Override
  public <X extends E> List<X> find(Class<X> cls, RepositoryExecContext<X> context) {
    String alias = getClassAlias(cls);
    CriteriaBuilder<X> cb = getBuilder(cls).from(cls, alias);
    TypedQuery<Long> countQuery = cb.getQueryRootCountQuery();
    if (context != null) {
      if (context instanceof BlazeExecContext) {
        ((BlazeExecContext<X>) context).init(cls, alias, cb);
      }
      for (RepositoryExecContextListener listener : context.getListeners()) {
        if (listener instanceof PageCriteriaDecorator) {
          //get count query before page-ing;
          countQuery = cb.getQueryRootCountQuery();
        }
        if (listener instanceof CriteriaDecorator decorator) {
          decorator.decorate();
        }
      }
      long total = Long.MIN_VALUE;
      for (ExecContext.Listener listener : context.getListeners()) {
        if (listener instanceof QueryExecContextListener) {
          ((QueryExecContextListener) listener).onQueryPrepared(cb.getQueryString());
        }
        if (listener instanceof TotalCountExecContextListener) {
          if (total == Long.MIN_VALUE) {
            total = countQuery.getSingleResult();
          }
          ((TotalCountExecContextListener) listener).onTotalCount(total);
        }
      }
    }
    log.debug("Executing find [{}].", cb.getQueryString());
    return cb.getResultList();
  }

  @Override
  public List<E> find(RepositoryExecContext<E> context) {
    return find(getRootClass(), context);
  }

  @Override
  public <S extends E> void refresh(Collection<S> entities) {
    for (S entity : entities) {
      em.flush();
      em.refresh(entity);
    }
  }

  @Override
  public List<E> find() {
    return find(null);
  }

  @Override
  public int deleteById(ID id) {
    return deleteById(List.of(id));
  }

  @Override
  public int deleteById(Collection<? extends ID> ids) {
    Class<E> tClass = getRootClass();
    String alias = getClassAlias(tClass);
    DeleteCriteriaBuilder<E> cb = cbf.delete(em, getRootClass(), alias);
    if (HasCode.class.isAssignableFrom(tClass)) {
      cb.where(alias + ".code").in(ids);
    } else if (HasUuid.class.isAssignableFrom(tClass)) {
      cb.where(alias + ".uuid").in(ids);
    }
    return cb.executeUpdate();
  }

  @Override
  public <S extends E> List<S> save(Collection<S> entities) {
    List<S> persisted = new ArrayList<>();
    for (S entity : entities) {
      em.persist(entity);
      persisted.add(entity);
    }
    return persisted;
  }

  @Override
  public <S extends E> S save(S entity) {
    return this.save(List.of(entity)).getFirst();
  }

  @Override
  public <S extends E> List<S> delete(Collection<S> entities) {
    List<S> removed = new ArrayList<>();
    for (S entity : entities) {
      em.remove(entity);
      removed.add(entity);
    }
    return removed;
  }

  @Override
  public <S extends E> S delete(S entity) {
    return this.delete(List.of(entity)).getFirst();
  }
}
