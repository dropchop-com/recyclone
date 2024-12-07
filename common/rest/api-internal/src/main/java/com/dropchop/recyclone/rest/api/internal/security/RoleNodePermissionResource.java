package com.dropchop.recyclone.rest.api.internal.security;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.RoleNodePermissionParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.rest.api.DynamicExecContext;
import com.dropchop.recyclone.rest.api.MediaType;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Path(Paths.Security.ROLE_NODE_PERMISSION)
@DynamicExecContext(value = RoleNodePermissionParams.class, internal = true)
@RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.VIEW)
public interface RoleNodePermissionResource<P extends RoleNodePermission> {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result <P> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  List <P> getRest();

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result <P> getById(@PathParam("id") UUID id);

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(MediaType.APPLICATION_JSON)
  List <P> getByIdRest(@PathParam("id") UUID id);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result <P> search(RoleNodePermissionParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  List <P> searchRest(RoleNodePermissionParams params);

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.CREATE)
  Result <P> create(List <P> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.CREATE)
  List <P> createRest(List <P> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.UPDATE)
  Result <P> update(List <P> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.UPDATE)
  List <P> updateRest(List <P> roles);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.DELETE)
  Result <P> delete(List <P> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.DELETE)
  List <P> deleteRest(List <P> objects);
  
}

