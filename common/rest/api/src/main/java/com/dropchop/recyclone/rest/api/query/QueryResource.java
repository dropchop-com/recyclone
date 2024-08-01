package com.dropchop.recyclone.rest.api.query;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.query.Query;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.api.DynamicExecContext;
import com.dropchop.recyclone.rest.api.MediaType;
import jakarta.ws.rs.*;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * REST interface for managing query operations.
 */
@Path(Paths.SEARCH_SEGMENT)
@DynamicExecContext(CodeParams.class)
@RequiresPermissions(Domains.Query.QUERY + PERM_DELIM + Actions.VIEW)
public interface QueryResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Query> getAll();

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Query> getById(@PathParam("id") String id);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Query> create(Query query);

  @PUT
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Query> update(@PathParam("id") String id, Query query);

  @DELETE
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Query> delete(@PathParam("id") String id);

  @POST
  @Path("execute")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<?> executeQuery(Query params);

}