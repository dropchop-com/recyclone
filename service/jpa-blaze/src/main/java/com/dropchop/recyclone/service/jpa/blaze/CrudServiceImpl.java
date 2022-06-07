package com.dropchop.recyclone.service.jpa.blaze;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.HasLanguageCode;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.base.DtoCode;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.entity.jpa.marker.HasELanguage;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeExecContext;
import com.dropchop.recyclone.service.api.CrudService;
import com.dropchop.recyclone.service.api.EntityByIdService;
import com.dropchop.recyclone.service.api.JoinEntityHelper;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.invoke.CommonExecContext;
import com.dropchop.recyclone.service.api.invoke.FilteringDtoContext;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.mapping.EntityCreationDelegate;
import com.dropchop.recyclone.service.jpa.blaze.localization.LanguageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 03. 22.
 */
@Slf4j
public abstract class CrudServiceImpl<D extends Dto, P extends Params, E extends Entity, ID>
  implements CrudService<D, P>, EntityByIdService<D, E, ID> {

  @Inject
  ServiceSelector serviceSelector;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContext<P, D> ctx;

  public abstract ServiceConfiguration<D, P, E, ID> getConfiguration();

  @Override
  public Class<E> getRootClass() {
    return getConfiguration().getRepository().getRootClass();
  }

  @Override
  public Optional<E> findById(D dto) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    if (dto instanceof DtoCode) {
      //noinspection unchecked
      return conf.getRepository().findById((ID)((DtoCode) dto).getCode());
    } else if (dto instanceof DtoId) {
      //noinspection unchecked
      return conf.getRepository().findById((ID)((DtoId) dto).getUuid());
    } else {
      throw new RuntimeException("findById(" + dto + ") is not supported.");
    }
  }

  @Override
  public List<E> findById(List<ID> ids) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    return conf.getRepository().findById(ids);
  }

  @Override
  public Optional<E> findById(ID id) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    return conf.getRepository().findById(id);
  }

  @Override
  public List<E> findAll() {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    return conf.getRepository().find();
  }

  protected void checkDtoPermissions(List<D> dtos) {
    Subject subject = ctx.getSubject();
    for (D dto : dtos) {
      if (!subject.isPermitted(ctx.getSecurityDomainAction(dto.identifier()))) {
        throw new ServiceException(ErrorCode.authorization_error, "Not permitted!",
          Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
      }
    }
  }

  protected List<E> findAndFilter(Function<E, E> filter, RepositoryExecContext<E, P> repositoryExecContext) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    List<E> entites = conf.getRepository().find(repositoryExecContext);
    if (filter != null) {
      for (Iterator<E> iterator = entites.listIterator(); iterator.hasNext(); ) {
        E entity = iterator.next();
        E tmp = filter.apply(entity);
        if (tmp == null) {
          iterator.remove();
        }
      }
    }
    return entites;
  }

  protected List<E> find(RepositoryExecContext<E, P> repositoryExecContext) {
    return findAndFilter(null, repositoryExecContext);
  }

  protected <JD extends Dto, JE extends Entity, JID> JoinEntityHelper<E, JD, JE, JID> getJoinHelper(EntityByIdService<JD, JE, JID> service) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    return new JoinEntityHelper<>(service,
      find(new BlazeExecContext<E, P>()
        .of(ctx)
        .criteriaDecorators(conf.getCriteriaDecorators())
      )
    );
  }

  @Override
  public Result<D> search() {
    Subject subject = ctx.getSubject();
    if (subject.isPermitted(ctx.getSecurityDomainAction())) {
      log.trace("search [{}] is permitted to view [{}]!", subject.getPrincipal(), ctx.getParams());
    }
    MappingContext<P> mapContext = new FilteringDtoContext<P>().of(ctx);
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    List<E> entities = findAndFilter(
      e -> !subject.isPermitted(ctx.getSecurityDomainAction(e.identifier())) ? null : e, new BlazeExecContext<E, P>()
      .of(ctx)
      .listener(mapContext)
      .criteriaDecorators(conf.getCriteriaDecorators()));
    return conf.getToDtoMapper().toDtosResult(entities, mapContext);
  }

  protected MappingContext<P> constructToEntityMappingContext(ServiceConfiguration<D, P, E, ID> conf) {
    MappingContext<P> context = new FilteringDtoContext<P>()
      .of(ctx)
      .listener(
        new AfterSetModificationListener<>()
      )
      .listener(
        new EntityCreationDelegate<D, E, ID, P>(this)
          .forActionOnly(Constants.Actions.UPDATE)
          .forActionOnly(Constants.Actions.DELETE)
      );

    Class<?> rootClass = conf.getRepository().getRootClass();
    if (
      HasELanguage.class.isAssignableFrom(rootClass) &&
      HasLanguageCode.class.isAssignableFrom(rootClass)
    ) {
      context.listener(
        new AfterSetLanguageListener<>(serviceSelector.select(LanguageService.class))
      );
    }

    return context;
  }

  protected Result<D> save(List<D> dtos) {
    checkDtoPermissions(dtos);
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    MappingContext<P> mapContext = constructToEntityMappingContext(conf);
    List<E> entities = conf.getToEntityMapper().toEntities(dtos, mapContext);
    conf.getRepository().save(entities);
    return conf.getToDtoMapper().toDtosResult(entities, mapContext);
  }

  @Override
  @Transactional
  public Result<D> create(List<D> dtos) {
    return save(dtos);
  }

  @Override
  @Transactional
  public Result<D> update(List<D> dtos) {
    return save(dtos);
  }

  @Override
  @Transactional
  public Result<D> delete(List<D> dtos) {
    checkDtoPermissions(dtos);
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    MappingContext<P> mapContext = constructToEntityMappingContext(conf);
    List<E> entities = conf.getToEntityMapper().toEntities(dtos, mapContext);
    conf.getRepository().delete(entities);
    return conf.getToDtoMapper().toDtosResult(entities, mapContext);
  }
}
