package com.dropchop.recyclone.rest.jaxrs.api.intern.security;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.rest.Constants.Tags;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.*;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Path(Paths.Security.ACTION)
@DynamicExecContext(CodeParams.class)
public interface ActionResource extends ClassicRestResource<Action> {

  @GET
  @Path("")
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Action> get();

  @GET
  @Path("")
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Action> getRest() {
    return unwrap(get());
  }

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Action> getByCode(@PathParam("code") String code);

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Action> getByCodeRest(@PathParam("code") String code) {
    return unwrap(getByCode(code));
  }

  @POST
  @Path(Paths.SEARCH)
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Action> search(CodeParams params);

  @POST
  @Path(Paths.SEARCH)
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Action> searchRest(CodeParams params) {
    return unwrap(search(params));
  }

  @POST
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Action> create(List<Action> objects);

  @POST
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Action> createRest(List<Action> actions) {
    return unwrap(create(actions));
  }

  @PUT
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Action> update(List<Action> objects);

  @PUT
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Action> updateRest(List<Action> actions) {
    return unwrap(update(actions));
  }

  @DELETE
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Action> delete(List<Action> objects);

  @DELETE
  @Tag(name = Tags.SECURITY)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Action> deleteRest(List<Action> actions) {
    return unwrap(delete(actions));
  }
}
