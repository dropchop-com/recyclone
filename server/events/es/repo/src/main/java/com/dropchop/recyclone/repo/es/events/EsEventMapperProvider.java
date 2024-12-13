package com.dropchop.recyclone.repo.es.events;

import com.dropchop.recyclone.base.api.mapper.MapToEntityMapper;
import com.dropchop.recyclone.mapper.es.events.EventMapToEntityMapper;
import com.dropchop.recyclone.mapper.es.events.EventToDtoMapper;
import com.dropchop.recyclone.mapper.es.events.EventToEsMapper;
import com.dropchop.recyclone.base.dto.model.event.Event;
import com.dropchop.recyclone.model.entity.es.events.EsEvent;
import com.dropchop.recyclone.base.api.repo.FilteringElasticMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
@Getter
@ApplicationScoped
@SuppressWarnings({"unused"})
public class EsEventMapperProvider extends FilteringElasticMapperProvider<Event, EsEvent, UUID>
    implements MapToEntityMapper<EsEvent> {

  @Inject
  EsEventRepository repository;

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
