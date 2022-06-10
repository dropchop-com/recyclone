package com.dropchop.recyclone.rest.jaxrs.api.intern.localization;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.rest.Constants.Tags;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.*;
import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 01. 22.
 */
@Path(Paths.Localization.LANGUAGE)
@DynamicExecContext(value = CodeParams.class, dataClass = Language.class)
@RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.VIEW)
public interface LanguageResource extends ClassicRestResource<Language> {

  @POST
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.CREATE)
  Result<Language> create(List<Language> objects);

  @POST
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.CREATE)
  default List<Language> createRest(List<Language> languages) {
    return unwrap(create(languages));
  }

  @PUT
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.UPDATE)
  Result<Language> update(List<Language> objects);

  @PUT
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.UPDATE)
  default List<Language> updateRest(List<Language> languages) {
    return unwrap(update(languages));
  }

  @DELETE
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.DELETE)
  Result<Language> delete(List<Language> objects);

  @DELETE
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.DELETE)
  default List<Language> deleteRest(List<Language> languages) {
    return unwrap(delete(languages));
  }
}
