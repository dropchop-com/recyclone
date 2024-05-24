package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.*;
import com.dropchop.recyclone.model.api.invoke.ResultFilter.ContentFilter;
import com.dropchop.recyclone.model.api.rest.Constants.ContentDetail;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.base.DtoCode;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.repo.api.CrudRepository;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.mapper.api.FilteringDtoContext;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.service.api.mapping.EntityDelegateFactory;
import com.dropchop.recyclone.mapper.api.SetEntityDeactivated;
import com.dropchop.recyclone.mapper.api.SetEntityModification;
import com.dropchop.recyclone.service.api.security.AuthorizationService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 03. 22.
 */
@Slf4j
public abstract class CrudServiceImpl<D extends Dto, E extends Entity, ID>
  implements CrudService<D>, EntityByIdService<D, E, ID> {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContextContainer ctxContainer;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  AuthorizationService authorizationService;

  public abstract ServiceConfiguration<D, E, ID> getConfiguration();

  @Override
  public Class<E> getRootClass() {
    return getConfiguration().getRepository().getRootClass();
  }

  @Override
  public E findById(D dto) {
    ServiceConfiguration<D, E, ID> conf = getConfiguration();
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
  public List<E> findById(Collection<ID> ids) {
    ServiceConfiguration<D, E, ID> conf = getConfiguration();
    return conf.getRepository().findById(ids);
  }

  @Override
  public E findById(ID id) {
    ServiceConfiguration<D, E, ID> conf = getConfiguration();
    return conf.getRepository().findById(id);
  }

  @Override
  public List<E> find() {
    ServiceConfiguration<D, E, ID> conf = getConfiguration();
    return conf.getRepository().find();
  }

  protected void checkDtoPermissions(List<D> dtos) {
    for (D dto : dtos) {
      if (!authorizationService.isPermitted(ctxContainer.get().getSecurityDomainAction(dto.identifier()))) {
        throw new ServiceException(ErrorCode.authorization_error, "Not permitted!",
          Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
      }
    }
  }

  protected MappingContext getMappingContextForRead() {
    MappingContext context = new FilteringDtoContext().of(ctxContainer.get());
    log.debug("Created mapping context [{}] for reading from execution context [{}].", context, ctxContainer.get());
    return context;
  }

  protected MappingContext getMappingContextForModify() {
    ServiceConfiguration<D, E, ID> conf = getConfiguration();
    Class<?> rootClass = conf.getRepository().getRootClass();
    MappingContext context = new FilteringDtoContext()
      .of(ctxContainer.get())
      .createWith(
        new EntityDelegateFactory<>(this)
          .forActionOnly(Constants.Actions.UPDATE)
          .forActionOnly(Constants.Actions.DELETE)
      )
      .afterMapping(
        new SetEntityModification(rootClass)
      )
      .afterMapping(
        new SetEntityDeactivated(rootClass)
      );
    log.debug("Created mapping context [{}] for modification from execution context [{}].", context, ctxContainer.get());
    return context;
  }

  protected abstract Iterable<CriteriaDecorator> getCommonCriteriaDecorators();

  protected abstract RepositoryExecContext<E> getRepositoryExecContext();

  protected RepositoryExecContext<E> getRepositoryExecContextWithTotalCount(MappingContext mapContext) {
    RepositoryExecContext<E> context = getRepositoryExecContext();
    //TODO: fix
    //context.totalCount(mapContext); // get total count and save it
    return context;
  }

  protected List<E> find(RepositoryExecContext<E> repositoryExecContext) {
    ServiceConfiguration<D, E, ID> conf = getConfiguration();
    return conf.getRepository().find(repositoryExecContext);
  }

  @Override
  public Result<D> search() {
    MappingContext mapContext = getMappingContextForRead();
    ServiceConfiguration<D, E, ID> conf = getConfiguration();
    List<E> entities = find(getRepositoryExecContextWithTotalCount(mapContext));
    entities = entities.stream().filter(
      e -> authorizationService.isPermitted(ctxContainer.get().getSecurityDomainAction(e.identifier()))
    ).collect(Collectors.toList());

    @SuppressWarnings("UnnecessaryLocalVariable")
    Result<D> result = conf.getToDtoMapper().toDtosResult(entities, mapContext);
    return result;
  }

  protected boolean shouldRefreshAfterSave() {
    Params params = ctxContainer.get().getParams();
    String cDetail = null;
    Integer cLevel = null;
    if (params instanceof CommonParams<?, ?, ?, ?> commonParams) {
      ResultFilter<?, ?> resultFilter = commonParams.getFilter();
      if (resultFilter != null) {
        ContentFilter contentFilter = resultFilter.getContent();
        if (contentFilter != null) {
          cDetail = contentFilter.getDetailLevel();
          cLevel = contentFilter.getTreeLevel();
        }
      }
      ResultFilterDefaults defaults = commonParams.getFilterDefaults();
      if (defaults != null) {
        if (cDetail == null) {
          cDetail = defaults.getDetailLevel();
        }
        if (cLevel == null) {
          cLevel = defaults.getTreeLevel();
        }
      }
    }
    if (cDetail == null) {
      cDetail = ContentDetail.NESTED_OBJS_IDCODE;
    }
    if (cLevel == null) {
      cLevel = 1;
    }
    if (cLevel >= 2) {
      return true;
    }
    return cLevel == 1 &&
      (
        ContentDetail.ALL_OBJS_IDCODE_TITLE.equals(cDetail) ||
        ContentDetail.NESTED_OBJS_IDCODE.equals(cDetail) ||
        ContentDetail.NESTED_OBJS_IDCODE_TITLE.equals(cDetail)
      );
  }

  protected void save(Collection<E> entities) {
    ServiceConfiguration<D, E, ID> conf = getConfiguration();
    CrudRepository<E, ID> repository = conf.getRepository();
    repository.save(entities);
    if (shouldRefreshAfterSave()) {
      repository.refresh(entities);
    }
  }

  protected Result<D> createOrUpdate(List<D> dtos) {
    checkDtoPermissions(dtos);
    ServiceConfiguration<D, E, ID> conf = getConfiguration();
    MappingContext mapContext = getMappingContextForModify();
    List<E> entities = conf.getToEntityMapper().toEntities(dtos, mapContext);
    save(entities);
    return conf.getToDtoMapper().toDtosResult(entities, mapContext);
  }

  @Override
  @Transactional
  public Result<D> create(List<D> dtos) {
    return createOrUpdate(dtos);
  }

  @Override
  @Transactional
  public Result<D> update(List<D> dtos) {
    return createOrUpdate(dtos);
  }

  @Override
  @Transactional
  public Result<D> delete(List<D> dtos) {
    checkDtoPermissions(dtos);
    ServiceConfiguration<D, E, ID> conf = getConfiguration();
    MappingContext mapContext = getMappingContextForModify();
    List<E> entities = conf.getToEntityMapper().toEntities(dtos, mapContext);
    conf.getRepository().delete(entities);
    return conf.getToDtoMapper().toDtosResult(entities, mapContext);
  }
}
