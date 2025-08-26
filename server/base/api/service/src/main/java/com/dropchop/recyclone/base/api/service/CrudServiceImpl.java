package com.dropchop.recyclone.base.api.service;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.invoke.*;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter.ContentFilter;
import com.dropchop.recyclone.base.api.model.rest.Constants.ContentDetail;
import com.dropchop.recyclone.base.api.repo.CrudRepository;
import com.dropchop.recyclone.base.api.repo.FilteringMapperProvider;
import com.dropchop.recyclone.base.api.service.security.AuthorizationService;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 03. 22.
 */
@Slf4j
public abstract class CrudServiceImpl<D extends Dto, E extends Entity, ID> implements CrudService<D> {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  AuthorizationService authorizationService;

  public abstract CrudRepository<E, ID> getRepository();

  public abstract FilteringMapperProvider<D, E, ID> getMapperProvider();

  public abstract CommonExecContext<D, ?> getExecutionContext();

  protected void checkDtoPermissions(List<D> dtos) {
    for (D dto : dtos) {
      CommonExecContext<?, ?> ctx = getExecutionContext();
      if (ctx.hasRequiredPermissions()) {
        if (!authorizationService.isPermitted(ctx.getSecurityDomainAction(dto.identifier()))) {
          throw new ServiceException(ErrorCode.authorization_error, "Not permitted!",
              Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
        }
      }
    }
  }

  @Override
  @Transactional
  public Result<D> search() {
    CrudRepository<E, ID> repository = getRepository();
    FilteringMapperProvider<D, E, ?> mapperProvider = getMapperProvider();
    MappingContext mapContext = mapperProvider.getMappingContextForRead();
    List<E> entities = repository.find(repository.getRepositoryExecContext(mapContext));
    CommonExecContext<?, ?> ctx = getExecutionContext();
    if (ctx.hasRequiredPermissions()) {
      entities = entities.stream().filter(
          e -> authorizationService.isPermitted(ctx.getSecurityDomainAction(e.identifier()))
      ).collect(Collectors.toList());
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    Result<D> result = mapperProvider.getToDtoMapper().toDtosResult(entities, mapContext);
    return result;
  }


  protected boolean shouldRefreshAfterSave() {
    Params params = getExecutionContext().getParams();
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
    CrudRepository<E, ID> repository = getRepository();
    repository.save(entities);
    if (shouldRefreshAfterSave()) {
      repository.refresh(entities);
    }
  }


  protected Result<D> createOrUpdate(List<D> dtos) {
    checkDtoPermissions(dtos);
    FilteringMapperProvider<D, E, ?> mapperProvider = getMapperProvider();
    MappingContext mapContext = mapperProvider.getMappingContextForModify();
    List<E> entities = mapperProvider.getToEntityMapper().toEntities(dtos, mapContext);
    save(entities);
    @SuppressWarnings("UnnecessaryLocalVariable")
    Result<D> result = mapperProvider.getToDtoMapper().toDtosResult(entities, mapContext);
    return result;
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
    FilteringMapperProvider<D, E, ?> mapperProvider = getMapperProvider();
    MappingContext mapContext = mapperProvider.getMappingContextForModify();
    List<E> entities = mapperProvider.getToEntityMapper().toEntities(dtos, mapContext);
    getRepository().delete(entities);
    return mapperProvider.getToDtoMapper().toDtosResult(entities, mapContext);
  }
}
