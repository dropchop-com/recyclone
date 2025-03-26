package com.dropchop.recyclone.base.es.repo.events;

import com.dropchop.recyclone.base.es.model.events.EsEvent;
import com.dropchop.recyclone.base.es.repo.ElasticRepository;
import com.dropchop.recyclone.base.es.repo.mapper.ElasticQueryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.elasticsearch.client.RestClient;

import java.util.UUID;

import static com.dropchop.recyclone.base.api.repo.config.DateBasedIndexConfig.MONTH_FORMATTER;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
@Getter
@ApplicationScoped
@SuppressWarnings({"unused", "CdiInjectionPointsInspection"})
public class EsEventRepository extends ElasticRepository<EsEvent, UUID> {

  private final Class<EsEvent> rootClass = EsEvent.class;

  @Inject
  ObjectMapper objectMapper;

  @Inject
  RestClient elasticsearchClient;

  @Inject
  ElasticQueryMapper elasticQueryMapper;

  EsEventIndexConfig elasticIndexConfig = EsEventIndexConfig
      .builder()
      .rootClass(getRootClass())
      .ingestPipeline("event_ingest_pipeline")
      .indexPostfix(MONTH_FORMATTER::format)
      .build();
}
