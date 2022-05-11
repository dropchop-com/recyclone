package com.dropchop.recyclone.rest.jaxrs.api.intern.localization;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.rest.Constants.Tags;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.*;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 01. 22.
 */
@Path(Paths.Localization.LANGUAGE)
@DynamicExecContext(dataClass = Language.class)
public interface LanguageResource extends ClassicRestResource<Language> {

  @POST
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Language> create(List<Language> objects);

  @POST
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Language> createRest(List<Language> languages) {
    return unwrap(create(languages));
  }

  @PUT
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Language> update(List<Language> objects);

  @PUT
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Language> updateRest(List<Language> languages) {
    return unwrap(update(languages));
  }

  @DELETE
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Language> delete(List<Language> objects);

  @DELETE
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Language> deleteRest(List<Language> languages) {
    return unwrap(delete(languages));
  }
}
