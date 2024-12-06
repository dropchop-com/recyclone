package com.dropchop.recyclone.service.es.events;

import com.dropchop.recyclone.model.dto.event.Event;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.entity.es.event.EsEvent;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.es.events.EventMapperProvider;
import com.dropchop.recyclone.repo.es.events.EventRepository;
import com.dropchop.recyclone.service.api.CrudElasticServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;
import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_ES;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */

@Slf4j
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_ES)
@SuppressWarnings("unused")
public class EventService extends CrudElasticServiceImpl<Event, EsEvent, UUID>
    implements com.dropchop.recyclone.service.api.events.EventService {


  @Inject
  EventRepository repository;

  @Inject
  EventMapperProvider filteringMapperProvider;


  @Override
  public Result<Event> update(List<Event> dtos) {
    return null;
  }
}
