package com.dropchop.recyclone.rest.jaxrs.api.intern.localization;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.rest.Constants.Tags;
import com.dropchop.recyclone.model.dto.localization.Country;
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
@Path(Paths.Localization.COUNTRY)
@DynamicExecContext(dataClass = Country.class)
public interface CountryResource extends ClassicRestResource<Country> {

  @POST
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Country> create(List<Country> objects);

  @POST
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Country> createRest(List<Country> languages) {
    return unwrap(create(languages));
  }

  @PUT
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Country> update(List<Country> objects);

  @PUT
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Country> updateRest(List<Country> languages) {
    return unwrap(update(languages));
  }

  @DELETE
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Country> delete(List<Country> objects);

  @DELETE
  @Tag(name = Tags.LOCALIZATION)
  @Tag(name = Tags.DynamicContext.INTERNAL)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Country> deleteRest(List<Country> languages) {
    return unwrap(delete(languages));
  }
}
