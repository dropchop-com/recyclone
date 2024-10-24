package com.dropchop.recyclone.quarkus.it.repo.es;

import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.repo.es.ElasticRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.elasticsearch.client.RestClient;

import java.util.Map;

@ApplicationScoped
public class ElasticDummyRepository extends ElasticRepository<Dummy, String> {

  @Inject
  ObjectMapper objectMapper;

  @Inject
  RestClient elasticsearchClient;

  Class<Dummy> rootClass = Dummy.class;

  @Override
  public Class<Dummy> getRootClass() {
    return rootClass;
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
  protected Dummy convertMapToEntity(Map<String, Object> source) {
    Dummy dummy = new Dummy();
    dummy.setCode((String) source.get("code"));
    dummy.setTitle((String) source.get("title"));
    dummy.setDescription((String) source.get("description"));
    dummy.setLang((String) source.get("lang"));
    return dummy;
  }

  @Override
  protected String getEntityId(Dummy entity) {
    return entity.getCode();
  }

}
