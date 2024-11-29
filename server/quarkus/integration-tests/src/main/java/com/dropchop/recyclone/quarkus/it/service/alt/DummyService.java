package com.dropchop.recyclone.quarkus.it.service.alt;

import com.dropchop.recyclone.model.api.invoke.CommonExecContext;
import com.dropchop.recyclone.model.api.invoke.CommonExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaDummy;
import com.dropchop.recyclone.quarkus.it.repo.DummyRepository;
import com.dropchop.recyclone.quarkus.it.repo.es.ElasticDummyRepository;
import com.dropchop.recyclone.quarkus.it.repo.jpa.DummyMapperProvider;
import com.dropchop.recyclone.repo.api.listener.QuerySearchResultListener;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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
    try {
      String json = objectMapper.writeValueAsString(queryParams.getCondition());
      log.info("Got query params: [{}]", json);
    } catch (Exception e) {
      throw new ServiceException(ErrorCode.data_validation_error, "Error extracting query params!", e);
    }
    return new Result<>("my-query-alternative");
  }

  @Override
  public Result<Dummy> esSearch() {
    CommonExecContext<Dummy, ?> context = ctxContainer.get();
    QueryParams queryParams = context.getParams();
    try {
      List<Dummy> actualResults = new java.util.ArrayList<>(Collections.emptyList());
      elasticRepository.setQuerySearchResultListener(new QuerySearchResultListener() {

        @Override
        public <S> void onResult(S result) {
          try {
            actualResults.add(mapperProvider.getMapToDtoMapper().fromMap((Map<String, String>) result));
          } catch (ServiceException e) {
            throw new ServiceException(ErrorCode.data_validation_error, "Error mapping from Map<String, String> to Dummy: ", e);
          }
        }
      });

      elasticRepository.search(queryParams, elasticRepository.getRepositoryExecContext());
      return new Result<Dummy>().toSuccess(actualResults, actualResults.size());
    } catch (ServiceException e) {
      throw new ServiceException(ErrorCode.data_validation_error, "Error extracting query params!", e);
    }
  }

  @Override
  public List<Dummy> esSave() {
    CommonExecContext<Dummy, ?> context = ctxContainer.get();

    return elasticRepository.save(context.getData());
  }

  @Override
  public List<Dummy> esDelete() {
    CommonExecContext<Dummy, ?> context = ctxContainer.get();
    return elasticRepository.delete(context.getData());
  }
}
