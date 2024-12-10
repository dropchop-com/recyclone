package com.dropchop.recyclone.service.es.events;

import com.dropchop.recyclone.model.dto.event.Event;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.entity.es.event.EsEvent;
import com.dropchop.recyclone.repo.es.events.EsEventMapperProvider;
import com.dropchop.recyclone.repo.es.events.EsEventRepository;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_ES_DEFAULT;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */

@Slf4j
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_ES_DEFAULT)
@SuppressWarnings("unused")
public class EventService extends CrudServiceImpl<Event, EsEvent, UUID>
    implements com.dropchop.recyclone.service.api.events.EventService {


  @Inject
  EsEventRepository repository;

  @Inject
  EsEventMapperProvider mapperProvider;

}
