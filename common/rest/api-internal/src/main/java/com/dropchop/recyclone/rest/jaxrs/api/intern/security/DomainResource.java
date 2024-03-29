package com.dropchop.recyclone.rest.jaxrs.api.intern.security;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Path(Paths.Security.DOMAIN)
@DynamicExecContext(value = CodeParams.class, dataClass = Domain.class, internal = true)
@RequiresPermissions(Domains.Security.DOMAIN + PERM_DELIM + Actions.VIEW)
public interface DomainResource extends ClassicRestResource<Domain> {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Domain> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Domain> getRest() {
    return unwrap(get());
  }

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Domain> getByCode(@PathParam("code") String code);

  @GET
  @Path("{code : [a-z_\\-.]{3,255}}")
  @Produces(MediaType.APPLICATION_JSON)
  default List<Domain> getByCodeRest(@PathParam("code") String code) {
    return unwrap(getByCode(code));
  }

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<Domain> search(CodeParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  default List<Domain> searchRest(CodeParams params) {
    return unwrap(search(params));
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.DOMAIN + PERM_DELIM + Actions.CREATE)
  Result<Domain> create(List<Domain> objects);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.DOMAIN + PERM_DELIM + Actions.CREATE)
  default List<Domain> createRest(List<Domain> domains) {
    return unwrap(create(domains));
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.DOMAIN + PERM_DELIM + Actions.UPDATE)
  Result<Domain> update(List<Domain> objects);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.DOMAIN + PERM_DELIM + Actions.UPDATE)
  default List<Domain> updateRest(List<Domain> domains) {
    return unwrap(update(domains));
  }

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Security.DOMAIN + PERM_DELIM + Actions.DELETE)
  Result<Domain> delete(List<Domain> objects);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Security.DOMAIN + PERM_DELIM + Actions.DELETE)
  default List<Domain> deleteRest(List<Domain> domains) {
    return unwrap(delete(domains));
  }
}
