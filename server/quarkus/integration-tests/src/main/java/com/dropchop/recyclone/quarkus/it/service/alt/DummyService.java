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
import com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper;
import com.dropchop.recyclone.repo.es.mapper.ElasticSearchResult;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
      // Convert the query parameters to the JSON query string for Elasticsearch
      String json = objectMapper.writeValueAsString(ElasticQueryMapper.elasticQueryMapper(queryParams));

      // Execute the search using the elasticRepository
      Response response = elasticRepository.search(json);
      log.info("Response from Elasticsearch: [{}]", response.toString());

      // Deserialize the Elasticsearch response into a list of Dummy objects

      ElasticSearchResult<Dummy> searchResult = objectMapper.readValue(
        response.getEntity().getContent(),
        new TypeReference<ElasticSearchResult<Dummy>>() {}
      );

      // Extract the list of Dummy objects from the response
      List<Dummy> results = searchResult.getHits().getHits().stream()
        .map(ElasticSearchResult.Hit::getSource)
        .collect(Collectors.toList());

      // Create and return a successful Result with the data
      return new Result<Dummy>().toSuccess(results, results.size());
    } catch (IOException e) {
      throw new ServiceException(ErrorCode.data_validation_error, "Error processing the Elasticsearch response!", e);
    } catch (Exception e) {
      throw new ServiceException(ErrorCode.data_validation_error, "Error extracting query params!", e);
    }
  }
}
