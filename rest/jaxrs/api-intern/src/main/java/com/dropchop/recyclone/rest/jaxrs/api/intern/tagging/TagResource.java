package com.dropchop.recyclone.rest.jaxrs.api.intern.tagging;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.rest.Constants.Tags;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;

import javax.ws.rs.*;
import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 01. 22.
 */
@Path(Paths.Tagging.TAG)
@DynamicExecContext(value = IdentifierParams.class, dataClass = Tag.class)
@RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.VIEW)
public interface TagResource extends ClassicRestResource<Tag<TitleTranslation>> {

  @POST
  @org.eclipse.microprofile.openapi.annotations.tags.Tag(name = Tags.TAGGING)
  @org.eclipse.microprofile.openapi.annotations.tags.Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.CREATE)
  Result<Tag<TitleTranslation>> create(List<Tag<TitleTranslation>> objects);

  @POST
  @org.eclipse.microprofile.openapi.annotations.tags.Tag(name = Tags.TAGGING)
  @org.eclipse.microprofile.openapi.annotations.tags.Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.CREATE)
  default List<Tag<TitleTranslation>> createRest(List<Tag<TitleTranslation>> tags) {
    return unwrap(create(tags));
  }

  @PUT
  @org.eclipse.microprofile.openapi.annotations.tags.Tag(name = Tags.TAGGING)
  @org.eclipse.microprofile.openapi.annotations.tags.Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.UPDATE)
  Result<Tag<TitleTranslation>> update(List<Tag<TitleTranslation>> tags);

  @PUT
  @org.eclipse.microprofile.openapi.annotations.tags.Tag(name = Tags.TAGGING)
  @org.eclipse.microprofile.openapi.annotations.tags.Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.UPDATE)
  default List<Tag<TitleTranslation>> updateRest(List<Tag<TitleTranslation>> tags) {
    return unwrap(update(tags));
  }

  @DELETE
  @org.eclipse.microprofile.openapi.annotations.tags.Tag(name = Tags.TAGGING)
  @org.eclipse.microprofile.openapi.annotations.tags.Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.DELETE)
  Result<Tag<TitleTranslation>> delete(List<Tag<TitleTranslation>> tags);

  @DELETE
  @org.eclipse.microprofile.openapi.annotations.tags.Tag(name = Tags.TAGGING)
  @org.eclipse.microprofile.openapi.annotations.tags.Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.DELETE)
  default List<Tag<TitleTranslation>> deleteRest(List<Tag<TitleTranslation>> tags) {
    return unwrap(delete(tags));
  }
}
