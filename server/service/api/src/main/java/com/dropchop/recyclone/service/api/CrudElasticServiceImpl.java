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
import com.dropchop.recyclone.repo.api.CrudRepository;
import com.dropchop.recyclone.repo.api.ElasticCrudRepository;
import com.dropchop.recyclone.repo.api.FilteringElasticMapperProvider;

import com.dropchop.recyclone.repo.api.listener.QuerySearchResultListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 28. 11. 24
 **/
@Slf4j
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

    MappingContext mappingContext = new FilteringDtoContext().of(ctxContainer.get());

    try {
      List<D> results = new ArrayList<>(Collections.emptyList());
      getRepository().setQuerySearchResultListener(new QuerySearchResultListener() {

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

      getRepository().search(params, getRepository().getRepositoryExecContext());
      return new Result<D>().toSuccess(results, results.size());
    } catch (ServiceException e) {
      throw new ServiceException(ErrorCode.data_validation_error, "Error extracting query params!", e);
    }
  }
}
