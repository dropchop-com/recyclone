package com.dropchop.recyclone.quarkus.it.repo.es;

import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.es.ElasticRepository;
import com.dropchop.recyclone.repo.api.listener.QuerySearchResultListener;
import com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;

@Slf4j
@ApplicationScoped
public class ElasticDummyRepository extends ElasticRepository<Dummy, String> {

  @Inject
  ObjectMapper objectMapper;

  @Inject
  RestClient elasticsearchClient;

  @Inject
  ElasticQueryMapper queryMapper;

  @Override
  public Class<Dummy> getRootClass() {
    return Dummy.class;
  }

  @Override
  protected ElasticQueryMapper getElasticQueryMapper() {
    return queryMapper;
  }

  @Override
  protected ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  @Override
  protected RestClient getElasticsearchClient() {
    return elasticsearchClient;
  }

  @PostConstruct
  public void initialize() {
    this.setQuerySearchResultListener(new QuerySearchResultListener() {
      @Override
      public <S> void onResult(S result) {
        log.info("Custom processing for result: {}", result);
      }
    });
  }
}
