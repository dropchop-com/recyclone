package com.dropchop.recyclone.base.api.jaxrs.localization;

import com.dropchop.recyclone.base.api.model.rest.Constants.Paths;
import com.dropchop.recyclone.base.api.model.rest.DynamicExecContext;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.localization.DictionaryTerm;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import jakarta.ws.rs.*;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Path(Paths.Localization.DICTIONARY_TERM)
@DynamicExecContext(CodeParams.class)
public interface DictionaryTermResource {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<DictionaryTerm> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  List<DictionaryTerm> getRest();

  @GET
  @Path("{code : [a-z]{255}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<DictionaryTerm> getByCode(@PathParam("code") String code);

  @GET
  @Path("{code : [a-z]{255}}")
  @Produces(MediaType.APPLICATION_JSON)
  List<DictionaryTerm> getByCodeRest(@PathParam("code") String code);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<DictionaryTerm> search(CodeParams parameters);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  List<DictionaryTerm> searchRest(CodeParams parameters);
}
