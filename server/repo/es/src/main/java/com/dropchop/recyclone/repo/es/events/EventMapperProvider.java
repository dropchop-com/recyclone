package com.dropchop.recyclone.repo.es.events;

import com.dropchop.recyclone.mapper.api.MapToEntityMapper;
import com.dropchop.recyclone.model.dto.event.Event;
import com.dropchop.recyclone.model.entity.es.event.EsEvent;
import com.dropchop.recyclone.repo.api.FilteringElasticMapperProvider;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.UUID;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
public class EventMapperProvider extends FilteringElasticMapperProvider<Event, EsEvent, UUID>
    implements MapToEntityMapper<EsEvent> {

  @Inject
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