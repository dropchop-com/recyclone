package com.dropchop.recyclone.repo.es.events;

import com.dropchop.recyclone.model.entity.es.events.EsEvent;
import com.dropchop.recyclone.repo.es.ElasticRepository;
import com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.elasticsearch.client.RestClient;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
@Getter
@ApplicationScoped
@SuppressWarnings({"unused", "CdiInjectionPointsInspection"})
public class EsEventRepository extends ElasticRepository<EsEvent, UUID> {

  Class<EsEvent> rootClass = EsEvent.class;

  @Inject
  ObjectMapper objectMapper;

  @Inject
  RestClient elasticsearchClient;

  @Inject
  ElasticQueryMapper elasticQueryMapper;
}
