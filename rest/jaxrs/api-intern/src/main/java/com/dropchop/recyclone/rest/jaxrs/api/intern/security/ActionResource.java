package com.dropchop.recyclone.rest.jaxrs.api.intern.security;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Path(Paths.Security.ACTION)
@DynamicExecContext(value = CodeParams.class, dataClass = Action.class, internal = true)
@RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.VIEW)
public interface ActionResource extends ClassicRestResource<Action> {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Action> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Action> getRest() {
    return unwrap(get());
  }

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Action> getByCode(@PathParam("code") String code);

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Action> getByCodeRest(@PathParam("code") String code) {
    return unwrap(getByCode(code));
  }

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Action> search(CodeParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Action> searchRest(CodeParams params) {
    return unwrap(search(params));
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.CREATE)
  Result<Action> create(List<Action> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.CREATE)
  default List<Action> createRest(List<Action> actions) {
    return unwrap(create(actions));
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.UPDATE)
  Result<Action> update(List<Action> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.UPDATE)
  default List<Action> updateRest(List<Action> actions) {
    return unwrap(update(actions));
  }

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.DELETE)
  Result<Action> delete(List<Action> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.DELETE)
  default List<Action> deleteRest(List<Action> actions) {
    return unwrap(delete(actions));
  }
}
