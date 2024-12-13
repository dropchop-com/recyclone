package com.dropchop.recyclone.quarkus.it.repo.es;

import com.dropchop.recyclone.quarkus.it.model.entity.es.EsDummy;
import com.dropchop.recyclone.repo.es.ElasticRepository;
import com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;

@Slf4j
@ApplicationScoped
public class ElasticDummyRepository extends ElasticRepository<EsDummy, String> {

  @Inject
  ObjectMapper objectMapper;

  @Inject
  RestClient elasticsearchClient;

  @Inject
  ElasticQueryMapper queryMapper;

  @Override
  public Class<EsDummy> getRootClass() {
    return EsDummy.class;
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

  @Override
  public <S> String getIndexName(S entity) {
    return super.getIndexName(entity);
  }
}
