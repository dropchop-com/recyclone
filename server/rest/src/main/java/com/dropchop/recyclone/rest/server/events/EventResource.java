package com.dropchop.recyclone.rest.server.events;

import com.dropchop.recyclone.model.dto.event.Event;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.invoke.EventParams;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.server.*;
import com.dropchop.recyclone.service.api.RecycloneType;
import com.dropchop.recyclone.service.api.events.EventService;
import com.dropchop.recyclone.service.api.localization.CountryService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_ES_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 01. 22.
 */
@Slf4j
@Getter
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class EventResource extends ClassicReadResource<Event, EventParams> implements
    com.dropchop.recyclone.rest.api.internal.events.EventResource {

  @Inject
  @RecycloneType(RECYCLONE_ES_DEFAULT)
  EventService service;

  @Inject
  EventParams queryParams;

  @Override
  public Result<Event> create(List<Event> data) {
    return service.create(data);
  }


  @Override
  public List<Event> createRest(List<Event> data) {
    return this.unwrap(this.create(data));
  }


  @Override
  public Result<Event> delete(List<Event> data) {
    return service.delete(data);
  }


  @Override
  public List<Event> deleteRest(List<Event> data) {
    return this.unwrap(this.delete(data));
  }


  @Override
  public Result<Event> update(List<Event> data) {
    return service.update(data);
  }



  @Override
  public List<Event> updateRest(List<Event> data) {
    return this.unwrap(this.update(data));
  }


  @Override
  public Result<Event> get() {
    return service.search();
  }


  @Override
  public Result<Event> search(EventParams parameters) {
    return service.search();
  }
}
