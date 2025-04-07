package com.dropchop.recyclone.base.api.service;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.repo.CrudRepository;
import com.dropchop.recyclone.base.api.repo.FilteringMapperProvider;
import com.dropchop.recyclone.base.api.repo.ctx.RepositoryExecContext;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.es.model.base.EsEntity;
import com.dropchop.recyclone.base.es.repo.ElasticRepository;
import com.dropchop.recyclone.base.es.repo.listener.AggregationResultListener;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class ElasticCrudServiceImpl<D extends Dto, E extends EsEntity, ID> extends CrudServiceImpl<D, E, ID> {

  @Override
  @Transactional
  public Result<D> search() {
    ProfileTimer timer = new ProfileTimer();

    log.debug("Searching for articles");
    CrudRepository<E, ID> repository = getRepository();
    FilteringMapperProvider<D, E, ?> mapperProvider = getMapperProvider();
    MappingContext mapContext = mapperProvider.getMappingContextForRead();


    // Create aggregation collector
    HashMap<String, Object> aggregations = new LinkedHashMap<>();

    RepositoryExecContext<E> context = repository.getRepositoryExecContext(mapContext);
    context.listener((AggregationResultListener) aggregations::put);


    List<E> entities = repository.find(context);
    log.debug("Found {} entities in [{}]ms", entities.size(), timer.mark());

    entities = entities.stream()
      .filter(e -> authorizationService.isPermitted(
        getExecutionContext().getSecurityDomainAction(e.identifier())
      ))
      .collect(Collectors.toList());

    Result<D> result = mapperProvider.getToDtoMapper().toDtosResult(entities, mapContext);
    result.setAggregations(aggregations);
    log.debug("Mapped {} entities in [{}]ms", result.getData().size(), timer.stop());
    return result;
  }

  @Transactional
  public Integer deleteById() {
    CommonExecContext<D, ?> context = getExecutionContext();
    CodeParams codeParams = context.getParams();
    List<ID> ids = (List<ID>) codeParams.getCodes();
    return getRepository().deleteById(ids);
  }

  @Transactional
  public Integer deleteByQuery() {
    RepositoryExecContext<E> context = getRepository().getRepositoryExecContext();
    if(getRepository() instanceof ElasticRepository<?,?>) {
      return ((ElasticRepository<E, ID>)getRepository()).deleteByQuery(context);
    }
    throw new ServiceException(
      ErrorCode.internal_error,
      "Only ElasticRepository has deleteByQuery!"
    );
  }
}
