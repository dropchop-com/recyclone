package com.dropchop.recyclone.rest.api.internal.security;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.rest.api.DynamicExecContext;
import com.dropchop.recyclone.rest.api.MediaType;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Path(Paths.Security.PERMISSION)
@DynamicExecContext(value = IdentifierParams.class, internal = true)
@RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.VIEW)
public interface PermissionResource {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Permission> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  List<Permission> getRest();

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Permission> getById(@PathParam("id") UUID id);

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(MediaType.APPLICATION_JSON)
  List<Permission> getByIdRest(@PathParam("id") UUID id);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Permission> search(IdentifierParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  List<Permission> searchRest(IdentifierParams params);

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.CREATE)
  Result<Permission> create(List<Permission> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.CREATE)
  List<Permission> createRest(List<Permission> permissions);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.UPDATE)
  Result<Permission> update(List<Permission> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.UPDATE)
  List<Permission> updateRest(List<Permission> permissions);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.DELETE)
  Result<Permission> delete(List<Permission> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.DELETE)
  List<Permission> deleteRest(List<Permission> permissions);
}
