package com.dropchop.recyclone.rest.api.internal.security;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
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
public interface RoleNodePermissionResource {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result <RoleNodePermission> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  List <RoleNodePermission> getRest();

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result <RoleNodePermission> getById(@PathParam("id") UUID id);

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(MediaType.APPLICATION_JSON)
  List <RoleNodePermission> getByIdRest(@PathParam("id") UUID id);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result <RoleNodePermission> search(RoleNodePermissionParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  List <RoleNodePermission> searchRest(RoleNodePermissionParams params);

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.CREATE)
  Result <RoleNodePermission> create(List <RoleNodePermission> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.CREATE)
  List <RoleNodePermission> createRest(List <RoleNodePermission> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.UPDATE)
  Result <RoleNodePermission> update(List <RoleNodePermission> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.UPDATE)
  List <RoleNodePermission> updateRest(List <RoleNodePermission> roles);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.DELETE)
  Result <RoleNodePermission> delete(List <RoleNodePermission> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.DELETE)
  List <RoleNodePermission> deleteRest(List <RoleNodePermission> objects);
  
}

