package com.dropchop.recyclone.events.es.service;

import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.api.service.RecycloneType;
import com.dropchop.recyclone.base.dto.model.event.Event;
import com.dropchop.recyclone.base.es.model.events.EsEvent;
import com.dropchop.recyclone.base.es.repo.events.EsEventMapperProvider;
import com.dropchop.recyclone.base.es.repo.events.EsEventRepository;
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

  @Inject
  CommonExecContext<Event, ?> executionContext;
}
