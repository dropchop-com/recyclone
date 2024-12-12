package com.dropchop.recyclone.rest.server.events.intern;

import com.dropchop.recyclone.model.api.query.Condition;
import com.dropchop.recyclone.model.dto.event.Event;
import com.dropchop.recyclone.model.dto.invoke.EventParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.server.ClassicRestByIdResource;
import com.dropchop.recyclone.service.api.RecycloneType;
import com.dropchop.recyclone.service.api.events.EventService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_ES_DEFAULT;
import static com.dropchop.recyclone.model.api.query.Condition.field;
import static com.dropchop.recyclone.model.api.query.Condition.or;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 01. 22.
 */
@Slf4j
@Getter
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class EventResource extends ClassicRestByIdResource<Event, EventParams> implements
    com.dropchop.recyclone.rest.api.internal.events.EventResource {

  @Inject
  @RecycloneType(RECYCLONE_ES_DEFAULT)
  EventService service;

  @Inject
  EventParams params;

  @Override
  public Result<Event> create(List<Event> data) {
    return service.create(data);
  }

  @Override
  public Result<Event> delete(List<Event> data) {
    return service.delete(data);
  }

  @Override
  public Result<Event> update(List<Event> data) {
    return service.update(data);
  }

  @Override
  public Result<Event> get() {
    return service.search();
  }

  @Override
  public Result<Event> getById(UUID id) {
    Condition c = or(
        field("uuid", id.toString())
    );
    params.setCondition(c);
    return service.search();
  }

  @Override
  public Result<Event> search(EventParams parameters) {
    return service.search();
  }
}
