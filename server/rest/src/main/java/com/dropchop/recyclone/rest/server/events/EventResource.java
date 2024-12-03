package com.dropchop.recyclone.rest.server.events;

import com.dropchop.recyclone.model.dto.event.Event;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.server.ClassicReadByCodeResource;
import com.dropchop.recyclone.rest.server.ClassicReadByIdResource;
import com.dropchop.recyclone.service.api.localization.CountryService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 01. 22.
 */
@Slf4j
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class EventResource extends ClassicReadByIdResource<Event, IdentifierParams> implements
    com.dropchop.recyclone.rest.api.internal.events.EventResource {

  @Inject
  EventService service;

  @Inject
  IdentifierParams params;

  @Override
  public Result<Event> get() {
    return service.search();
  }

  @Override
  public Result<Event> getById(UUID id) {
    params.setIdentifiers(List.of(id.toString()));
    return service.search();
  }

  @Override
  public Result<Event> search(IdentifierParams parameters) {
    return service.search();
  }
}
