package com.dropchop.recyclone.rest.api.internal.localization;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.api.rest.DynamicExecContext;
import com.dropchop.recyclone.model.api.rest.MediaType;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 01. 22.
 */
@Path(Paths.Localization.COUNTRY)
@DynamicExecContext(value = CodeParams.class, internal = true)
@RequiresPermissions(Domains.Localization.COUNTRY + PERM_DELIM + Actions.VIEW)
public interface CountryResource {

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Localization.COUNTRY + PERM_DELIM + Actions.CREATE)
  Result<Country> create(List<Country> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Localization.COUNTRY + PERM_DELIM + Actions.CREATE)
  List<Country> createRest(List<Country> languages);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Localization.COUNTRY + PERM_DELIM + Actions.UPDATE)
  Result<Country> update(List<Country> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Localization.COUNTRY + PERM_DELIM + Actions.UPDATE)
  List<Country> updateRest(List<Country> languages);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Localization.COUNTRY + PERM_DELIM + Actions.DELETE)
  Result<Country> delete(List<Country> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Localization.COUNTRY + PERM_DELIM + Actions.DELETE)
  List<Country> deleteRest(List<Country> languages);
}
