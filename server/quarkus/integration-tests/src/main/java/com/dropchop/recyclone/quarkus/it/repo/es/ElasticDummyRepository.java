package com.dropchop.recyclone.quarkus.it.repo.es;

import com.dropchop.recyclone.quarkus.it.model.entity.es.EsDummy;
import com.dropchop.recyclone.base.es.repo.ElasticRepository;
import com.dropchop.recyclone.base.es.repo.mapper.ElasticQueryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;

@Slf4j
@Getter
@ApplicationScoped
@SuppressWarnings("unused")
public class ElasticDummyRepository extends ElasticRepository<EsDummy, String> {

  @Inject
  ObjectMapper objectMapper;

  @Inject
  RestClient elasticsearchClient;

  @Inject
  ElasticQueryMapper elasticQueryMapper;

  Class<EsDummy> rootClass = EsDummy.class;
}
