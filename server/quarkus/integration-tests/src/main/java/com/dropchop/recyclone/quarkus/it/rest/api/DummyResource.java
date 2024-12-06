package com.dropchop.recyclone.quarkus.it.rest.api;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.annotations.Logical;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.quarkus.it.model.api.Constants;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.model.dto.invoke.DummyQueryParams;
import com.dropchop.recyclone.rest.api.DynamicExecContext;
import com.dropchop.recyclone.rest.api.MediaType;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Path(Constants.Paths.Test.DUMMY)
@DynamicExecContext(value = CodeParams.class, dataClass = Dummy.class)
@RequiresPermissions(value = Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.VIEW, logical = Logical.OR)
public interface DummyResource {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Dummy> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  List<Dummy> getRest();

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Dummy> getByCode(@PathParam("code") String code);

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON)
  List<Dummy> getByCodeRest(@PathParam("code") String code);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Dummy> search(CodeParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  List<Dummy> searchRest(CodeParams params);

  @POST
  @Path(Paths.QUERY_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Dummy> query(DummyQueryParams params);

  @POST
  @Path(Paths.QUERY_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  List<Dummy> queryRest(DummyQueryParams params);

  @POST
  @Path(Paths.ELASTICSEARCH_SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  Result<Dummy> esSearch(DummyQueryParams params);
}
