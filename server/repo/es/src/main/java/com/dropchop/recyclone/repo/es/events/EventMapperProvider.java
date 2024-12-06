package com.dropchop.recyclone.repo.es.events;

import com.dropchop.recyclone.mapper.api.MapToEntityMapper;
import com.dropchop.recyclone.mapper.es.events.EventMapToEntityMapper;
import com.dropchop.recyclone.mapper.es.events.EventToDtoMapper;
import com.dropchop.recyclone.mapper.es.events.EventToEsMapper;
import com.dropchop.recyclone.model.dto.event.Event;
import com.dropchop.recyclone.model.entity.es.event.EsEvent;
import com.dropchop.recyclone.repo.api.FilteringElasticMapperProvider;
import com.dropchop.recyclone.repo.api.RepositoryType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_ES;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
@Getter
@ApplicationScoped
@RepositoryType(RECYCLONE_ES)
public class EventMapperProvider extends FilteringElasticMapperProvider<Event, EsEvent, UUID>
    implements MapToEntityMapper<EsEvent> {

  @Inject
  @RepositoryType(RECYCLONE_ES)
  EventRepository repository;

  @Inject
  EventToDtoMapper toDtoMapper;

  @Inject
  EventToEsMapper toEntityMapper;

  @Inject
  EventMapToEntityMapper mapToEntityMapper;

  @Override
  public EsEvent fromMap(Map<String, Object> result) {
    return null;
  }



}