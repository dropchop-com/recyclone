package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.mapper.api.FilteringDtoContext;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.CommonExecContext;
import com.dropchop.recyclone.model.api.invoke.CommonExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.repo.api.ElasticCrudRepository;
import com.dropchop.recyclone.repo.api.FilteringElasticMapperProvider;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.api.listener.MapQuerySearchResultListener;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 28. 11. 24
 **/
@Slf4j
@SuppressWarnings("unused")
public abstract class CrudElasticServiceImpl<D extends Dto, E extends Entity, ID> implements CrudService<D> {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContextContainer ctxContainer;

  public abstract ElasticCrudRepository<E, ID> getRepository();

  public abstract FilteringElasticMapperProvider<D, E, ID> getFilteringMapperProvider();

  @Override
  @Transactional
  @SuppressWarnings("unchecked")
  public Result<D> search() {
    CommonExecContext<D, ?> context = ctxContainer.get();
    QueryParams params;

    if(context != null) {
      params = context.getParams();
    } else {
      throw new ServiceException(ErrorCode.internal_error, "ExecContext is null!");
    }

    List<D> results = new ArrayList<>(Collections.emptyList());
    MappingContext mappingContext = new FilteringDtoContext().of(ctxContainer.get());
    RepositoryExecContext<E> ctx = getRepository().getRepositoryExecContext();

    ctx.listener(new MapQuerySearchResultListener() {
      @Override
      public <S> void onResult(S result) {
        try {
          E entity = (E) getFilteringMapperProvider().getMapToEntityMapper().fromMap((Map<String, Object>) result);
          results.add(getFilteringMapperProvider().getToDtoMapper().toDto(entity, mappingContext));
        } catch (ServiceException e) {
          throw new ServiceException(ErrorCode.data_validation_error,
            "Error mapping from Map<String, String> to Dummy: ", e);
        }
      }
    });

    try {
      getRepository().search(params, ctx);
      return new Result<D>().toSuccess(results, results.size());
    } catch (ServiceException e) {
      throw new ServiceException(ErrorCode.data_validation_error, "Error extracting query params!", e);
    }
  }

  @Override
  @Transactional
  public Result<D> create(List<D> dtos) {
    MappingContext mappingContext = new FilteringDtoContext().of(ctxContainer.get());

    List<E> entites = getFilteringMapperProvider().getToEntityMapper().toEntities(dtos, mappingContext);

    List<E> saved = getRepository().save(entites);

    return new Result<D>().toSuccess(getFilteringMapperProvider()
      .getToDtoMapper().toDtos(saved, mappingContext));
  }

  @Override
  @Transactional
  public Result<D> delete(List<D> dtos) {
    MappingContext mappingContext = new FilteringDtoContext().of(ctxContainer.get());

    List<E> entites = getFilteringMapperProvider().getToEntityMapper().toEntities(dtos, mappingContext);
    List<E> deleted = getRepository().delete(entites);

    return new Result<D>().toSuccess(getFilteringMapperProvider()
      .getToDtoMapper().toDtos(deleted, mappingContext));
  }

  @Override
  @Transactional
  public Result<D> update(List<D> dtos) {
    return this.create(dtos);
  }
}
