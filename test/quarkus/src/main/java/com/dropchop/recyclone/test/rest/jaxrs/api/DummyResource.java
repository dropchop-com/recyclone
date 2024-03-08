package com.dropchop.recyclone.test.rest.jaxrs.api;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import com.dropchop.recyclone.test.model.api.Constants;
import com.dropchop.recyclone.test.model.dto.Dummy;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Path(Constants.Paths.Test.DUMMY)
@DynamicExecContext(value = CodeParams.class, dataClass = Dummy.class, internal = true)
@RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.VIEW)
public interface DummyResource extends ClassicRestResource<Dummy> {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Dummy> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Dummy> getRest() {
    return unwrap(get());
  }

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Dummy> getByCode(@PathParam("code") String code);

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Dummy> getByCodeRest(@PathParam("code") String code) {
    return unwrap(getByCode(code));
  }

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Dummy> search(CodeParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Dummy> searchRest(CodeParams params) {
    return unwrap(search(params));
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.CREATE)
  Result<Dummy> create(List<Dummy> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.CREATE)
  default List<Dummy> createRest(List<Dummy> dummies) {
    return unwrap(create(dummies));
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.UPDATE)
  Result<Dummy> update(List<Dummy> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.UPDATE)
  default List<Dummy> updateRest(List<Dummy> dummies) {
    return unwrap(update(dummies));
  }

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.DELETE)
  Result<Dummy> delete(List<Dummy> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Constants.Domains.Test.DUMMY + PERM_DELIM + Actions.DELETE)
  default List<Dummy> deleteRest(List<Dummy> dummies) {
    return unwrap(delete(dummies));
  }
}
