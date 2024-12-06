package com.dropchop.recyclone.quarkus.it.service.alt;

import com.dropchop.recyclone.mapper.api.FilteringDtoContext;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.invoke.CommonExecContext;
import com.dropchop.recyclone.model.api.invoke.CommonExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.model.entity.es.EsDummy;
import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaDummy;
import com.dropchop.recyclone.quarkus.it.repo.DummyRepository;
import com.dropchop.recyclone.quarkus.it.repo.es.ElasticDummyRepository;
import com.dropchop.recyclone.quarkus.it.repo.jpa.DummyMapperProvider;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.api.listener.MapQuerySearchResultListener;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Slf4j
@Getter
@ApplicationScoped
@RecycloneType("alter")
@SuppressWarnings("unused")
public class DummyService extends CrudServiceImpl<Dummy, JpaDummy, String>
  implements com.dropchop.recyclone.quarkus.it.service.api.DummyService {

  @Inject
  @RecycloneType("alter")
  DummyRepository repository;

  @Inject
  DummyMapperProvider mapperProvider;

  @Inject
  ElasticDummyRepository elasticRepository;

  @Inject
  CommonExecContextContainer ctxContainer;

  @Inject
  ObjectMapper objectMapper;

  @Override
  public Result<Dummy> query() {
    CommonExecContext<Dummy, ?> context = ctxContainer.get();
    QueryParams queryParams = context.getParams();

    List<Dummy> actualResults = new java.util.ArrayList<>(Collections.emptyList());

    MappingContext map = new FilteringDtoContext().of(ctxContainer.get());
    RepositoryExecContext<? extends EsDummy> ctx = elasticRepository.getRepositoryExecContext();
    try {
      ctx.listener(
          new MapQuerySearchResultListener() {
            @Override
            public <S> void onResult(S result) {
              try {
                //noinspection unchecked
                actualResults.add(mapperProvider.getMapToDtoMapper().fromMap((Map<String, String>) result));
              } catch (ServiceException e) {
                throw new ServiceException(
                    ErrorCode.data_validation_error, "Error mapping from Map<String, String> to Dummy: ", e
                );
              }
            }
          }
      );
      elasticRepository.search(queryParams, ctx);
      return new Result<Dummy>().toSuccess(actualResults, actualResults.size());
    } catch (ServiceException e) {
      throw new ServiceException(ErrorCode.data_validation_error, "Error extracting query params!", e);
    }
  }

  @Override
  @Transactional
  protected Result<Dummy> createOrUpdate(List<Dummy> dtos) {
    Result<Dummy> result = super.createOrUpdate(dtos);
    MappingContext mapContext = mapperProvider.getMappingContextForModify();
    List<EsDummy> entities = mapperProvider.getToEsEntityMapper().toEntities(dtos, mapContext);
    elasticRepository.save(entities);
    return result;
  }

  @Override
  @Transactional
  public Result<Dummy> delete(List<Dummy> dtos) {
    Result<Dummy> result = super.delete(dtos);
    MappingContext mapContext = mapperProvider.getMappingContextForModify();
    List<EsDummy> entities = mapperProvider.getToEsEntityMapper().toEntities(dtos, mapContext);
    elasticRepository.delete(entities);
    return result;
  }
}
