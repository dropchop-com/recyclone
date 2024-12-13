package com.dropchop.recyclone.rest.api.localization;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.api.rest.DynamicExecContext;
import com.dropchop.recyclone.model.api.rest.MediaType;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Path(Paths.Localization.LANGUAGE)
@DynamicExecContext(CodeParams.class)
@RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.VIEW)
public interface LanguageResource {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Language> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  List<Language> getRest();

  @GET
  @Path("{code : [a-z]{2}(([\\-\\w])*(%2A)*)*}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Language> getByCode(@PathParam("code") String code);

  @GET
  @Path("{code : [a-z]{2}(([\\-\\w])*(%2A)*)*}")
  @Produces(MediaType.APPLICATION_JSON)
  List<Language> getByCodeRest(@PathParam("code") String code);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Language> search(CodeParams parameters);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  List<Language> searchRest(CodeParams parameters);
}
