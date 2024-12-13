package com.dropchop.recyclone.rest.api.internal.localization;

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
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 01. 22.
 */
@Path(Paths.Localization.LANGUAGE)
@DynamicExecContext(value = CodeParams.class, internal = true)
@RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.VIEW)
public interface LanguageResource {

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.CREATE)
  Result<Language> create(List<Language> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.CREATE)
  List<Language> createRest(List<Language> languages);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.UPDATE)
  Result<Language> update(List<Language> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.UPDATE)
  List<Language> updateRest(List<Language> languages);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.DELETE)
  Result<Language> delete(List<Language> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.DELETE)
  List<Language> deleteRest(List<Language> languages);
}
