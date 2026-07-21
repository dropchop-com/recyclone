package com.dropchop.recyclone.base.api.jaxrs.internal.localization;

import com.dropchop.recyclone.base.api.model.rest.Constants.Paths;
import com.dropchop.recyclone.base.api.model.rest.DynamicExecContext;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import com.dropchop.recyclone.base.api.model.security.Constants.Actions;
import com.dropchop.recyclone.base.api.model.security.Constants.Domains;
import com.dropchop.recyclone.base.api.model.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.invoke.TagParams;
import com.dropchop.recyclone.base.dto.model.localization.DictionaryTerm;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.base.api.model.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 01. 22.
 */
@Path(Paths.Localization.DICTIONARY_TERM)
@DynamicExecContext(value = CodeParams.class, internal = true)
@RequiresPermissions(Domains.Localization.DICTIONARY_TERM + PERM_DELIM + Actions.VIEW)
public interface DictionaryTermResource {

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Localization.DICTIONARY_TERM  + PERM_DELIM + Actions.CREATE)
  Result<DictionaryTerm> create(List<DictionaryTerm> terms);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Localization.DICTIONARY_TERM  + PERM_DELIM + Actions.CREATE)
  List<DictionaryTerm> createRest(List<DictionaryTerm> terms);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Localization.DICTIONARY_TERM  + PERM_DELIM + Actions.UPDATE)
  Result<DictionaryTerm> update(List<DictionaryTerm> terms);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Localization.DICTIONARY_TERM  + PERM_DELIM + Actions.UPDATE)
  List<DictionaryTerm> updateRest(List<DictionaryTerm> terms);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Localization.DICTIONARY_TERM  + PERM_DELIM + Actions.DELETE)
  Result<DictionaryTerm> delete(List<DictionaryTerm> terms);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Localization.DICTIONARY_TERM  + PERM_DELIM + Actions.DELETE)
  List<DictionaryTerm> deleteRest(List<DictionaryTerm> terms);
}
