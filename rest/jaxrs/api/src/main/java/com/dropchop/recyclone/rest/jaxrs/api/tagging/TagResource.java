package com.dropchop.recyclone.rest.jaxrs.api.tagging;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.rest.Constants.Tags;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.*;
import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Path(Paths.Tagging.TAG)
@DynamicExecContext(IdentifierParams.class)
@RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.VIEW)
public interface TagResource extends ClassicRestResource<com.dropchop.recyclone.model.dto.tagging.Tag<TitleTranslation>> {

  @GET
  @Path("")
  @Tag(name = Tags.TAGGING)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.IdentifierParams")
  Result<com.dropchop.recyclone.model.dto.tagging.Tag<TitleTranslation>> get();

  @GET
  @Path("")
  @Tag(name = Tags.TAGGING)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.IdentifierParams")
  default List<com.dropchop.recyclone.model.dto.tagging.Tag<TitleTranslation>> getRest() {
    return unwrap(get());
  }

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Tag(name = Tags.TAGGING)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.IdentifierParams")
  Result<com.dropchop.recyclone.model.dto.tagging.Tag<TitleTranslation>> getById(@PathParam("id") UUID id);

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Tag(name = Tags.TAGGING)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.IdentifierParams")
  default List<com.dropchop.recyclone.model.dto.tagging.Tag<TitleTranslation>> getByIdRest(@PathParam("id") UUID id) {
    return unwrap(getById(id));
  }

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Tag(name = Tags.TAGGING)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<com.dropchop.recyclone.model.dto.tagging.Tag<TitleTranslation>> search(IdentifierParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Tag(name = Tags.TAGGING)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<com.dropchop.recyclone.model.dto.tagging.Tag<TitleTranslation>> searchRest(IdentifierParams params) {
    return unwrap(search(params));
  }
}