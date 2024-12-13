package com.dropchop.recyclone.base.api.jaxrs.internal.security;

import com.dropchop.recyclone.base.api.model.rest.Constants.Paths;
import com.dropchop.recyclone.base.api.model.security.Constants.Actions;
import com.dropchop.recyclone.base.api.model.security.Constants.Domains;
import com.dropchop.recyclone.base.api.model.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.Action;
import com.dropchop.recyclone.base.api.model.rest.DynamicExecContext;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.base.api.model.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Path(Paths.Security.ACTION)
@DynamicExecContext(value = CodeParams.class, internal = true)
@RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.VIEW)
public interface ActionResource {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Action> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  List<Action> getRest();

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Action> getByCode(@PathParam("code") String code);

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON)
  List<Action> getByCodeRest(@PathParam("code") String code);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Action> search(CodeParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  List<Action> searchRest(CodeParams params);

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.CREATE)
  Result<Action> create(List<Action> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.CREATE)
  List<Action> createRest(List<Action> actions);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.UPDATE)
  Result<Action> update(List<Action> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.UPDATE)
  List<Action> updateRest(List<Action> actions);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.DELETE)
  Result<Action> delete(List<Action> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.DELETE)
  List<Action> deleteRest(List<Action> actions);
}
