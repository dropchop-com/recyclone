package com.dropchop.recyclone.repo.es.events;

import com.dropchop.recyclone.model.entity.es.event.EsEvent;
import com.dropchop.recyclone.model.entity.es.localization.EsCountry;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.es.ElasticRepository;
import com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.elasticsearch.client.RestClient;

import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;
import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_ES;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
@Getter
@ApplicationScoped
@RepositoryType(RECYCLONE_ES)
public class EventRepository extends ElasticRepository<EsEvent, UUID> {

  Class<EsEvent> rootClass = EsEvent.class;

  @Override
  protected ElasticQueryMapper getElasticQueryMapper() {
    return null;
  }

  @Override
  protected ObjectMapper getObjectMapper() {
    return null;
  }

  @Override
  protected RestClient getElasticsearchClient() {
    return null;
  }
}
