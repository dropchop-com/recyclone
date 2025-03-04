package com.dropchop.recyclone.base.api.jaxrs.internal.security;

import com.dropchop.recyclone.base.api.model.rest.Constants.Paths;
import com.dropchop.recyclone.base.api.model.security.Constants.Actions;
import com.dropchop.recyclone.base.api.model.security.Constants.Domains;
import com.dropchop.recyclone.base.api.model.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.base.dto.model.invoke.RoleNodeParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.RoleNode;
import com.dropchop.recyclone.base.api.model.rest.DynamicExecContext;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.security.Constants.PERM_DELIM;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Path(Paths.Security.ROLE_NODE)
@DynamicExecContext(value = RoleNodeParams.class, internal = true)
@RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.VIEW)
public interface RoleNodeResource {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<RoleNode> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  List<RoleNode> getRest();

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<RoleNode> getById(@PathParam("id") UUID id);

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(MediaType.APPLICATION_JSON)
  List<RoleNode> getByIdRest(@PathParam("id") UUID id);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<RoleNode> search(RoleNodeParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  List<RoleNode> searchRest(RoleNodeParams params);

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.CREATE)
  Result<RoleNode> create(List<RoleNode> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.CREATE)
  List<RoleNode> createRest(List<RoleNode> roles);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.UPDATE)
  Result<RoleNode> update(List<RoleNode> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.UPDATE)
  List<RoleNode> updateRest(List<RoleNode> roles);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.DELETE)
  Result<RoleNode> delete(List<RoleNode> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE_NODE + PERM_DELIM + Actions.DELETE)
  List<RoleNode> deleteRest(List<RoleNode> roles);

}

