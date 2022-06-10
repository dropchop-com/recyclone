package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.DeleteCriteriaBuilder;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.repo.api.CrudRepository;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;
import com.dropchop.recyclone.repo.api.ctx.QueryExecContextListener;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.api.ctx.TotalCountExecContextListener;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@Slf4j
public abstract class BlazeRepository<E, ID> implements CrudRepository<E, ID> {

  @Inject
  EntityManager em;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CriteriaBuilderFactory cbf;

  public String getRootAlias() {
    return getRootClass().getSimpleName().toLowerCase();
  }

  public CriteriaBuilder<E> getBuilder() {
    return cbf.create(em, getRootClass());
  }

  @Override
  public Optional<E> findById(ID id) {
    return findById(List.of(id)).stream().findFirst();
  }

  @Override
  public List<E> findById(Collection<ID> ids) {
    String alias = getRootAlias();
    Class<E> tClass = getRootClass();
    CriteriaBuilder<E> cb = getBuilder().from(getRootClass(), alias);
    if (HasCode.class.isAssignableFrom(tClass)) {
      cb.where(alias + ".code").in(ids);
    } else if (HasUuid.class.isAssignableFrom(tClass)) {
      cb.where(alias + ".uuid").in(ids);
    }
    return cb.getResultList();
  }

  @Override
  public <P extends Params> List<E> find(RepositoryExecContext<E, P> context) {
    String alias = getRootAlias();
    CriteriaBuilder<E> cb = getBuilder().from(getRootClass(), alias);
    TypedQuery<Long> countQuery = cb.getQueryRootCountQuery();
    if (context != null) {
      if (context instanceof BlazeExecContext) {
        ((BlazeExecContext<E, ?>) context).init(getRootClass(), alias, cb);
      }
      for (CriteriaDecorator<E> decorator : context.getCriteriaDecorators()) {
        if (decorator instanceof PageCriteriaDecorator) {
          //get count query before page-ing;
          countQuery = cb.getQueryRootCountQuery();
        }
        decorator.decorate();
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
    String alias = getRootAlias();
    Class<E> tClass = getRootClass();
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
    return this.save(List.of(entity)).get(0);
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
    return this.delete(List.of(entity)).get(0);
  }
}
