package com.dropchop.recyclone.rest.jaxrs.api.intern.security;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Path(Paths.Security.ROLE)
@DynamicExecContext(value = RoleParams.class, dataClass = Role.class, internal = true)
@RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.VIEW)
public interface RoleResource extends ClassicRestResource<Role> {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Role> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Role> getRest() {
    return unwrap(get());
  }

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Role> getByCode(@PathParam("code") String code);

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Role> getByCodeRest(@PathParam("code") String code) {
    return unwrap(getByCode(code));
  }

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Role> search(RoleParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Role> searchRest(RoleParams params) {
    return unwrap(search(params));
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.CREATE)
  Result<Role> create(List<Role> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.CREATE)
  default List<Role> createRest(List<Role> roles) {
    return unwrap(create(roles));
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  Result<Role> update(List<Role> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  default List<Role> updateRest(List<Role> roles) {
    return unwrap(update(roles));
  }

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.DELETE)
  Result<Role> delete(List<Role> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.DELETE)
  default List<Role> deleteRest(List<Role> roles) {
    return unwrap(delete(roles));
  }

  @PUT
  @Path(Paths.Security.PERMISSION_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  Result<Role> addPermissions(RoleParams params);

  @PUT
  @Path(Paths.Security.PERMISSION_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  default List<Role> addPermissionsRest(RoleParams params) {
    return unwrap(addPermissions(params));
  }

  @DELETE
  @Path(Paths.Security.PERMISSION_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  Result<Role> removePermissions(RoleParams params);

  @DELETE
  @Path(Paths.Security.PERMISSION_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  default List<Role> removePermissionsRest(RoleParams params) {
    return unwrap(removePermissions(params));
  }
}
