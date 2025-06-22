package com.dropchop.recyclone.base.jpa.repo;

import com.blazebit.persistence.BaseWhereBuilder;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.DeleteCriteriaBuilder;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;
import com.dropchop.recyclone.base.api.mapper.TotalCountExecContextListener;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.repo.CrudRepository;
import com.dropchop.recyclone.base.api.repo.ctx.CriteriaDecorator;
import com.dropchop.recyclone.base.api.repo.ctx.QueryExecContextListener;
import com.dropchop.recyclone.base.api.repo.ctx.RepositoryExecContext;
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
public abstract class BlazeRepository<E extends Model, ID> implements CrudRepository<E, ID> {

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

  protected <S extends E> Collection<BlazeCriteriaDecorator<S>> getCommonCriteriaDecorators() {
    return List.of(
        new LikeIdentifiersCriteriaDecorator<>(),
        new InlinedStatesCriteriaDecorator<>(),
        new SortCriteriaDecorator<>(),
        new PageCriteriaDecorator<>()
    );
  }

  public <S extends E> BlazeExecContext<S> createRepositoryExecContext() {
    Class<S> cls = getRootClass();
    String alias = getClassAlias(cls);
    ExecContext<?> sourceContext = ctxContainer.get();
    if (sourceContext != null) {
      return new BlazeExecContext<>(cls, alias, sourceContext);
    }
    return new BlazeExecContext<>(cls, alias);
  }

  @Override
  public <S extends E> BlazeExecContext<S> getRepositoryExecContext() {
    BlazeExecContext<S> context = createRepositoryExecContext();
    Collection<BlazeCriteriaDecorator<S>> decorators = getCommonCriteriaDecorators();
    for (BlazeCriteriaDecorator<S> decorator : decorators) {
      context.decorateWith(decorator);
    }
    return context;
  }

  @Override
  public <S extends E> BlazeExecContext<S> getRepositoryExecContext(MappingContext mappingContext) {
    BlazeExecContext<S> context = getRepositoryExecContext();
    return context.totalCount(mappingContext);
  }

  public <X extends E> CriteriaBuilder<X> getBuilder(Class<X> cls) {
    return cbf.create(em, cls);
  }

  protected void addIdCriteria(BaseWhereBuilder<?> cb, String alias, Class<?> tClass, Collection<?> ids) {
    if (HasCode.class.isAssignableFrom(tClass)) {
      cb.where(alias + ".code").in(ids);
    } else if (HasUuid.class.isAssignableFrom(tClass)) {
      cb.where(alias + ".uuid").in(ids);
    } else if (HasId.class.isAssignableFrom(tClass)) {
      cb.where(alias + ".id").in(ids);
    }
  }

  @Override
  public <S extends E, X extends ID> S findById(X id) {
    List<S> entities = findById(List.of(id));
    return entities.isEmpty() ? null : entities.getFirst();
  }

  @Override
  public <S extends E, X extends ID> List<S> findById(Collection<X> ids) {
    Class<S> tClass = getRootClass();
    String alias = getClassAlias(tClass);
    CriteriaBuilder<S> cb = getBuilder(tClass).from(getRootClass(), alias);
    addIdCriteria(cb, alias, tClass, ids);
    return cb.getResultList();
  }

  private static <E extends Model, S extends E> TypedQuery<Long> applyDecorators(RepositoryExecContext<S> context,
                                                                                 CriteriaBuilder<S> cb) {
    TypedQuery<Long> countQuery = cb.getQueryRootCountQuery();
    for (RepositoryExecContextListener listener : context.getListeners()) {
      if (listener instanceof PageCriteriaDecorator) {
        //get a count query before page-ing;
        countQuery = cb.getQueryRootCountQuery();
      }
      if (listener instanceof CriteriaDecorator<?, ?>) {
        @SuppressWarnings("unchecked")
        CriteriaDecorator<E, BlazeExecContext<E>> decorator = (CriteriaDecorator<E, BlazeExecContext<E>>) listener;
        decorator.decorate();
      }
    }
    return countQuery;
  }

  @Override
  public <S extends E> List<S> find(RepositoryExecContext<S> context) {
    if (!(context instanceof BlazeExecContext<S> blazeExecContext)) {
      throw new ServiceException(
          ErrorCode.parameter_validation_error,
          "Invalid context: Expected " + BlazeExecContext.class + " but received " + context.getClass()
      );
    }
    Class<S> cls = blazeExecContext.getRootClass();
    String alias = blazeExecContext.getRootAlias();
    CriteriaBuilder<S> cb = getBuilder(cls).from(cls, alias);
    blazeExecContext.init(cb);

    TypedQuery<Long> countQuery = applyDecorators(context, cb);
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
    //log.debug("Executing find [{}].", cb.getQueryString());
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
  public <S extends E> List<S> find() {
    return find(getRepositoryExecContext());
  }

  @Override
  public <X extends ID> int deleteById(X id) {
    return deleteById(List.of(id));
  }

  @Override
  public <X extends ID> int deleteById(Collection<X> ids) {
    Class<E> tClass = getRootClass();
    String alias = getClassAlias(tClass);
    DeleteCriteriaBuilder<E> cb = cbf.delete(em, getRootClass(), alias);
    addIdCriteria(cb, alias, tClass, ids);
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
