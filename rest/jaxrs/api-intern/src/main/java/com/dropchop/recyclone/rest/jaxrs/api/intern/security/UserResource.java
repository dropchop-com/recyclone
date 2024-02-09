package com.dropchop.recyclone.rest.jaxrs.api.intern.security;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.rest.Constants.Tags;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.invoke.UserParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.ws.rs.*;
import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Path(Paths.Security.USER)
@DynamicExecContext(value = IdentifierParams.class, dataClass = User.class)
@RequiresPermissions(Domains.Security.USER + PERM_DELIM + Actions.VIEW)
public interface UserResource extends ClassicRestResource<User<DtoId>> {

  @GET
  @Path("")
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.UserParams")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<User<DtoId>> get();

  @GET
  @Path("")
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.UserParams")
  @Produces(MediaType.APPLICATION_JSON)
  default List<User<DtoId>> getRest() {
    return unwrap(get());
  }

  @GET
  @Path("{id}")
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.UserParams")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<User<DtoId>> getByUuid(@PathParam("id") UUID id);

  @GET
  @Path("{id}")
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.UserParams")
  @Produces(MediaType.APPLICATION_JSON)
  default User<DtoId> getByUuidRest(@PathParam("id") UUID id) {
    return unwrapFirst(getByUuid(id));
  }

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<User<DtoId>> search(UserParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<User<DtoId>> searchRest(UserParams params) {
    return unwrap(search(params));
  }


  @POST
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.USER + PERM_DELIM + Actions.CREATE)
  Result<User<DtoId>> create(List<User<DtoId>> users);

  @POST
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.USER + PERM_DELIM + Actions.CREATE)
  default List<User<DtoId>> createRest(List<User<DtoId>> users) {
    return unwrap(create(users));
  }

  @PUT
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.USER + PERM_DELIM + Actions.UPDATE)
  Result<User<DtoId>> update(List<User<DtoId>> users);

  @PUT
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.USER + PERM_DELIM + Actions.UPDATE)
  default List<User<DtoId>> updateRest(List<User<DtoId>> users) {
    return unwrap(update(users));
  }

}
