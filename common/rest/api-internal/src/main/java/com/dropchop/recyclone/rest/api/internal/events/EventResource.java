package com.dropchop.recyclone.rest.api.internal.events;

import com.dropchop.recyclone.model.api.rest.Constants;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.event.Event;
import com.dropchop.recyclone.model.dto.invoke.EventParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.api.DynamicExecContext;
import com.dropchop.recyclone.rest.api.MediaType;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;
import static com.dropchop.recyclone.rest.api.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
@Path(Constants.Paths.Event.EVENTS)
@DynamicExecContext(value = EventParams.class, internal = true)
@RequiresPermissions(Domains.Events.EVENT + PERM_DELIM + Actions.VIEW)
public interface EventResource {

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Event> getById(@PathParam("id") UUID id);

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(APPLICATION_JSON)
  List<Event> getByIdRest(@PathParam("id") UUID id);

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Events.EVENT + PERM_DELIM + Actions.CREATE)
  Result<Event> create(List<Event> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Events.EVENT + PERM_DELIM + Actions.CREATE)
  List<Event> createRest(List<Event> languages);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Events.EVENT + PERM_DELIM + Actions.UPDATE)
  Result<Event> update(List<Event> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Events.EVENT + PERM_DELIM + Actions.UPDATE)
  List<Event> updateRest(List<Event> languages);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Events.EVENT + PERM_DELIM + Actions.DELETE)
  Result<Event> delete(List<Event> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Events.EVENT + PERM_DELIM + Actions.DELETE)
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
