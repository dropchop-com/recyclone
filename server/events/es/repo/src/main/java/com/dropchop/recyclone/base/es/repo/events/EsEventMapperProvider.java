package com.dropchop.recyclone.base.es.repo.events;

import com.dropchop.recyclone.base.api.repo.FilteringMapperProvider;
import com.dropchop.recyclone.base.dto.model.event.Event;
import com.dropchop.recyclone.base.es.mapper.events.EventToDtoMapper;
import com.dropchop.recyclone.base.es.mapper.events.EventToEsMapper;
import com.dropchop.recyclone.base.es.model.events.EsEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
@Getter
@ApplicationScoped
@SuppressWarnings({"unused"})
public class EsEventMapperProvider extends FilteringMapperProvider<Event, EsEvent, UUID> {

  @Inject
  EsEventRepository repository;

  @Inject
  EventToDtoMapper toDtoMapper;

  @Inject
  EventToEsMapper toEntityMapper;
}
