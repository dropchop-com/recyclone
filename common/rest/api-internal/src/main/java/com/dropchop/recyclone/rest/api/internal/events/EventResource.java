package com.dropchop.recyclone.rest.api.internal.events;

import com.dropchop.recyclone.model.api.rest.Constants;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.event.Event;
import com.dropchop.recyclone.model.dto.invoke.EventParams;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.api.DynamicExecContext;
import com.dropchop.recyclone.rest.api.MediaType;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
@Path(Constants.Paths.Event.EVENTS)
@DynamicExecContext(value = EventParams.class, internal = true)
@RequiresPermissions(com.dropchop.recyclone.model.api.security.Constants.Domains.Events.EVENT + PERM_DELIM + com.dropchop.recyclone.model.api.security.Constants.Actions.VIEW)
public interface EventResource {

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(com.dropchop.recyclone.model.api.security.Constants.Domains.Events.EVENT + PERM_DELIM + com.dropchop.recyclone.model.api.security.Constants.Actions.CREATE)
  Result<Event> create(List<Event> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(com.dropchop.recyclone.model.api.security.Constants.Domains.Events.EVENT + PERM_DELIM + com.dropchop.recyclone.model.api.security.Constants.Actions.CREATE)
  List<Event> createRest(List<Event> languages);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(com.dropchop.recyclone.model.api.security.Constants.Domains.Events.EVENT + PERM_DELIM + com.dropchop.recyclone.model.api.security.Constants.Actions.UPDATE)
  Result<Event> update(List<Event> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(com.dropchop.recyclone.model.api.security.Constants.Domains.Events.EVENT + PERM_DELIM + com.dropchop.recyclone.model.api.security.Constants.Actions.UPDATE)
  List<Event> updateRest(List<Event> languages);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(com.dropchop.recyclone.model.api.security.Constants.Domains.Events.EVENT + PERM_DELIM + com.dropchop.recyclone.model.api.security.Constants.Actions.DELETE)
  Result<Event> delete(List<Event> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(com.dropchop.recyclone.model.api.security.Constants.Domains.Events.EVENT + PERM_DELIM + com.dropchop.recyclone.model.api.security.Constants.Actions.DELETE)
  List<Event> deleteRest(List<Event> languages);

  @POST
  @Path(Constants.Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result <Event> search(EventParams params);

  @POST
  @Path(Constants.Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  List <Event> searchRest(EventParams params);
}
