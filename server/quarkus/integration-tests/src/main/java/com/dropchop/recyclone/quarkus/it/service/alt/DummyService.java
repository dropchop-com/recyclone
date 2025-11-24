package com.dropchop.recyclone.quarkus.it.service.alt;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.mapper.FilteringDtoContext;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContextContainer;
import com.dropchop.recyclone.base.api.repo.ctx.RepositoryExecContext;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
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
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Slf4j
@Getter
@ApplicationScoped
@RecycloneType("alter")
@SuppressWarnings({"unused", "CdiInjectionPointsInspection", "RedundantSuppression"})
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
  CommonExecContext<Dummy, ?> executionContext;

  @Inject
  ObjectMapper objectMapper;

  @Override
  public Result<Dummy> search() {
    CommonExecContext<Dummy, ?> context = ctxContainer.get();
    CodeParams codeParams = context.getParams();
    List<String> codes = codeParams.getCodes();
    List<EsDummy> entities = elasticRepository.findById(codes);
    MappingContext mapCtx = new FilteringDtoContext().of(ctxContainer.get());
    List<Dummy> dtos = mapperProvider.getToEsDtoMapper().toDtos(entities, mapCtx);
    return new Result<Dummy>().toSuccess(dtos, dtos.size());
  }

  @Override
  public Result<Dummy> query() {
    CommonExecContext<Dummy, ?> context = ctxContainer.get();
    QueryParams queryParams = context.getParams();

    List<Dummy> actualResults = new java.util.ArrayList<>(Collections.emptyList());
    MappingContext map = new MappingContext().of(ctxContainer.get());
    RepositoryExecContext<EsDummy> ctx = elasticRepository.getRepositoryExecContext();
    List<EsDummy> entities = elasticRepository.search(ctx);
    MappingContext mapCtx = new FilteringDtoContext().of(ctxContainer.get());
    List<Dummy> dtos = mapperProvider.getToEsDtoMapper().toDtos(entities, mapCtx);

    return new Result<Dummy>().toSuccess(actualResults, actualResults.size());
  }

  @Override
  @Transactional
  protected Result<Dummy> createOrUpdate(List<Dummy> dtos) {
    Result<Dummy> result = super.createOrUpdate(dtos);
    MappingContext mapContext = mapperProvider.getMappingContextForModify(executionContext);
    List<EsDummy> entities = mapperProvider.getToEsEntityMapper().toEntities(dtos, mapContext);
    elasticRepository.save(entities);
    return result;
  }

}
