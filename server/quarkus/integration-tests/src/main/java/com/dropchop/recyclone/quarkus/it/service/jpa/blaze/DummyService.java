package com.dropchop.recyclone.quarkus.it.service.jpa.blaze;

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
import com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Response;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Slf4j
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
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

  public Result<Dummy> esSearch() {
    CommonExecContext<Dummy, ?> context = ctxContainer.get();
    QueryParams queryParams = context.getParams();
    try {
      String json = objectMapper.writeValueAsString(ElasticQueryMapper.elasticQueryMapper(queryParams));
      Response result = elasticRepository.search(json);
      log.info("response is: [{}]", result.toString());
      return null;
    } catch (Exception e) {
      throw new ServiceException(ErrorCode.data_validation_error, "Error extracting query params!", e);
    }
  }
}
