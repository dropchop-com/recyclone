package com.dropchop.recyclone.events.es.service;

import com.dropchop.recyclone.base.dto.model.event.Event;
import com.dropchop.recyclone.model.entity.es.events.EsEvent;
import com.dropchop.recyclone.repo.es.events.EsEventMapperProvider;
import com.dropchop.recyclone.repo.es.events.EsEventRepository;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.api.service.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_ES_DEFAULT;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */

@Slf4j
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_ES_DEFAULT)
@SuppressWarnings("unused")
public class EventService extends CrudServiceImpl<Event, EsEvent, UUID>
    implements com.dropchop.recyclone.events.api.service.EventService {


  @Inject
  EsEventRepository repository;

  @Inject
  EsEventMapperProvider mapperProvider;

}
