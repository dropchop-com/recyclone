package com.dropchop.recyclone.rest.jaxrs.api.intern.security;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Path(Paths.Security.PERMISSION)
@DynamicExecContext(value = IdentifierParams.class, dataClass = Permission.class, internal = true)
@RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.VIEW)
public interface PermissionResource extends ClassicRestResource<Permission> {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Permission> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Permission> getRest() {
    return unwrap(get());
  }

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Permission> getById(@PathParam("id") UUID id);

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Permission> getByIdRest(@PathParam("id") UUID id) {
    return unwrap(getById(id));
  }

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Permission> search(IdentifierParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Permission> searchRest(IdentifierParams params) {
    return unwrap(search(params));
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.CREATE)
  Result<Permission> create(List<Permission> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.CREATE)
  default List<Permission> createRest(List<Permission> permissions) {
    return unwrap(create(permissions));
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.UPDATE)
  Result<Permission> update(List<Permission> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.UPDATE)
  default List<Permission> updateRest(List<Permission> permissions) {
    return unwrap(update(permissions));
  }

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.DELETE)
  Result<Permission> delete(List<Permission> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.PERMISSION + PERM_DELIM + Actions.DELETE)
  default List<Permission> deleteRest(List<Permission> permissions) {
    return unwrap(delete(permissions));
  }
}
