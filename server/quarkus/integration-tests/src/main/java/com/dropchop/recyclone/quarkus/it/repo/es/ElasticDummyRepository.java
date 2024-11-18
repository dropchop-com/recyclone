package com.dropchop.recyclone.quarkus.it.repo.es;

import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.repo.es.ElasticRepository;
import com.dropchop.recyclone.repo.es.listener.QuerySearchResultListener;
import com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;

import java.util.Map;

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
    // Adding a custom listener to log each search result
    this.setQuerySearchResultListener(new QuerySearchResultListener() {
      @Override
      public <S> void onResult(S result) {
        log.info("Custom processing for result: {}", result);
      }
    });
  }

  @Override
  protected Dummy convertMapToEntity(Map<String, Object> source) {
    Dummy dummy = new Dummy();
    dummy.setCode((String) source.get("code"));
    dummy.setTitle((String) source.get("title"));
    dummy.setDescription((String) source.get("description"));
    dummy.setLang((String) source.get("lang"));
    return dummy;
  }
}
