package com.dropchop.recyclone.rest.api.internal.security;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.api.rest.DynamicExecContext;
import com.dropchop.recyclone.model.api.rest.MediaType;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Path(Paths.Security.ROLE)
@DynamicExecContext(value = RoleParams.class, internal = true)
@RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.VIEW)
public interface RoleResource {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Role> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  List<Role> getRest();

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Role> getByCode(@PathParam("code") String code);

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON)
  List<Role> getByCodeRest(@PathParam("code") String code);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Role> search(RoleParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  List<Role> searchRest(RoleParams params);

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.CREATE)
  Result<Role> create(List<Role> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.CREATE)
  List<Role> createRest(List<Role> roles);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  Result<Role> update(List<Role> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  List<Role> updateRest(List<Role> roles);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.DELETE)
  Result<Role> delete(List<Role> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.DELETE)
  List<Role> deleteRest(List<Role> roles);

  @PUT
  @Path(Paths.Security.PERMISSION_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  Result<Role> addPermissions(RoleParams params);

  @PUT
  @Path(Paths.Security.PERMISSION_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  List<Role> addPermissionsRest(RoleParams params);

  @DELETE
  @Path(Paths.Security.PERMISSION_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  Result<Role> removePermissions(RoleParams params);

  @DELETE
  @Path(Paths.Security.PERMISSION_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  List<Role> removePermissionsRest(RoleParams params);
}
