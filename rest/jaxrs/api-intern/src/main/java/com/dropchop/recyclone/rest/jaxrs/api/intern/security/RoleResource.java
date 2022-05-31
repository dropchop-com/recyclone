package com.dropchop.recyclone.rest.jaxrs.api.intern.security;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.rest.Constants.Tags;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.*;
import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Path(Paths.Security.ROLE)
@DynamicExecContext(value = CodeParams.class, dataClass = Role.class)
@RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.VIEW)
public interface RoleResource extends ClassicRestResource<Role> {

  @GET
  @Path("")
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Role> get();

  @GET
  @Path("")
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
  default List<Role> getRest() {
    return unwrap(get());
  }

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Role> getByCode(@PathParam("code") String code);

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Role> getByCodeRest(@PathParam("code") String code) {
    return unwrap(getByCode(code));
  }

  @POST
  @Path(Paths.SEARCH)
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Role> search(CodeParams params);

  @POST
  @Path(Paths.SEARCH)
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Role> searchRest(CodeParams params) {
    return unwrap(search(params));
  }

  @POST
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.CREATE)
  Result<Role> create(List<Role> objects);

  @POST
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.CREATE)
  default List<Role> createRest(List<Role> roles) {
    return unwrap(create(roles));
  }

  @PUT
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  Result<Role> update(List<Role> objects);

  @PUT
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.UPDATE)
  default List<Role> updateRest(List<Role> roles) {
    return unwrap(update(roles));
  }

  @DELETE
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.DELETE)
  Result<Role> delete(List<Role> objects);

  @DELETE
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.ROLE + PERM_DELIM + Actions.DELETE)
  default List<Role> deleteRest(List<Role> roles) {
    return unwrap(delete(roles));
  }
}
