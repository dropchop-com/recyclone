package com.dropchop.recyclone.quarkus.it.repo.es;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.es.repo.ElasticRepository;
import com.dropchop.recyclone.base.es.repo.config.ClassStaticIndexConfig;
import com.dropchop.recyclone.base.es.repo.config.ElasticIndexConfig;
import com.dropchop.recyclone.base.es.repo.marker.AlwaysPresentDeleteFields;
import com.dropchop.recyclone.base.es.repo.marker.AlwaysPresentSearchFields;
import com.dropchop.recyclone.base.es.repo.query.ElasticQueryBuilder;
import com.dropchop.recyclone.quarkus.it.model.entity.es.EsDummy;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;

import java.util.Set;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

@Slf4j
@Getter
@ApplicationScoped
@SuppressWarnings("unused")
public class ElasticDummyRepository extends ElasticRepository<EsDummy, String>
  implements AlwaysPresentSearchFields, AlwaysPresentDeleteFields {
  //implements BlockAllDelete {

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
  ElasticQueryBuilder elasticQueryBuilder;

  @Override
  public Set<String> anyOf() {
    return Set.of("created");
  }
}
