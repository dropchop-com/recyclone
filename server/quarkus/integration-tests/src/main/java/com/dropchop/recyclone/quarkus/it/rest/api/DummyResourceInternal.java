package com.dropchop.recyclone.quarkus.it.rest.api;

import com.dropchop.recyclone.base.api.model.security.Constants.Actions;
import com.dropchop.recyclone.base.api.model.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.quarkus.it.model.api.Constants;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.base.api.model.rest.DynamicExecContext;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.base.api.model.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Path(Constants.Paths.Test.DUMMY)
@DynamicExecContext(value = CodeParams.class, internal = true)
@RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.VIEW)
public interface DummyResourceInternal {

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.CREATE)
  Result<Dummy> create(List<Dummy> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.CREATE)
  List<Dummy> createRest(List<Dummy> dummies);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.UPDATE)
  Result<Dummy> update(List<Dummy> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.UPDATE)
  List<Dummy> updateRest(List<Dummy> dummies);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.DELETE)
  Result<Dummy> delete(List<Dummy> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.DELETE)
  List<Dummy> deleteRest(List<Dummy> dummies);

  @DELETE
  @Path("/deleteById")
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.DELETE)
  int deleteById(CodeParams params);

  @DELETE
  @Path("/deleteByQuery")
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.DELETE)
  int deleteByQuery(QueryParams params);
}
