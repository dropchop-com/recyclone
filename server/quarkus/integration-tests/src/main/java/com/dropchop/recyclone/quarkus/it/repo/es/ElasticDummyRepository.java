package com.dropchop.recyclone.quarkus.it.repo.es;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.es.repo.config.ClassStaticIndexConfig;
import com.dropchop.recyclone.base.es.repo.config.ElasticIndexConfig;
import com.dropchop.recyclone.base.es.repo.ElasticRepository;
import com.dropchop.recyclone.base.es.repo.mapper.ElasticQueryMapper;
import com.dropchop.recyclone.quarkus.it.model.entity.es.EsDummy;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

@Slf4j
@Getter
@ApplicationScoped
@SuppressWarnings("unused")
public class ElasticDummyRepository extends ElasticRepository<EsDummy, String> {

  private final Class<EsDummy> rootClass = EsDummy.class;

  private final ElasticIndexConfig elasticIndexConfig = ClassStaticIndexConfig
      .builder()
      .rootClass(getRootClass())
      .build();

  @Inject
  @RecycloneType(RECYCLONE_DEFAULT)
  ObjectMapper objectMapper;

  @Inject
  RestClient elasticsearchClient;

  @Inject
  ElasticQueryMapper elasticQueryMapper;
}
