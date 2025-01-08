package com.dropchop.recyclone.quarkus.it.service.jpa.blaze;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContextContainer;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.api.service.RecycloneType;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.model.entity.es.EsDummy;
import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaDummy;
import com.dropchop.recyclone.quarkus.it.repo.DummyRepository;
import com.dropchop.recyclone.quarkus.it.repo.es.ElasticDummyRepository;
import com.dropchop.recyclone.quarkus.it.repo.jpa.DummyMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Slf4j
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings({"unused", "CdiInjectionPointsInspection"})
public class DummyService extends CrudServiceImpl<Dummy, JpaDummy, String>
  implements com.dropchop.recyclone.quarkus.it.service.api.DummyService {

  @Inject
  DummyRepository repository;

  @Inject
  ElasticDummyRepository elasticRepository;

  @Inject
  DummyMapperProvider mapperProvider;

  @Inject
  CommonExecContextContainer ctxContainer;

  @Inject
  CommonExecContext<Dummy, ?> executionContext;

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
    return new Result<>("my-query-jpa-blaze");
  }

  @Override
  protected Result<Dummy> createOrUpdate(List<Dummy> dtos) {
    Result<Dummy> result = super.createOrUpdate(dtos);
    MappingContext mapContext = mapperProvider.getMappingContextForModify();
    List<EsDummy> entities = mapperProvider.getToEsEntityMapper().toEntities(dtos, mapContext);
    elasticRepository.save(entities);
    return result;
  }

  @Override
  public Result<Dummy> delete(List<Dummy> dtos) {
    Result<Dummy> result = super.delete(dtos);
    MappingContext mapContext = mapperProvider.getMappingContextForModify();
    List<EsDummy> entities = mapperProvider.getToEsEntityMapper().toEntities(dtos, mapContext);
    elasticRepository.delete(entities);
    return result;
  }

  @Override
  public int delete() {
    return 0;
  }

  @Override
  public int deleteByQuery() {
    return 0;
  }
}
