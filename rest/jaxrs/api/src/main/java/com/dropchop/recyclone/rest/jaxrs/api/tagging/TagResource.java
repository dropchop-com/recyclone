package com.dropchop.recyclone.rest.jaxrs.api.tagging;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.TagParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Path(Paths.Tagging.TAG)
@DynamicExecContext(TagParams.class)
@RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.VIEW)
public interface TagResource<T extends com.dropchop.recyclone.model.dto.tagging.Tag> extends ClassicRestResource<T> {

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<T> get();

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  default List<T> getRest() {
    return unwrap(get());
  }

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<T> getById(@PathParam("id") UUID id);

  @GET
  @Path("{id: [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}}")
  @Produces(MediaType.APPLICATION_JSON)
  default List<T> getByIdRest(@PathParam("id") UUID id) {
    return unwrap(getById(id));
  }

  @GET
  @Path("{type: [A-Z][a-zA-Z\\d]{2,}(%2A)*}")
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<T> getByType(@PathParam("type") String type);

  @GET
  @Path("{type: [A-Z][a-zA-Z\\d*]{2,}(%2A)*}")
  @Produces(MediaType.APPLICATION_JSON)
  default List<T> getByTypeRest(@PathParam("type") String type) {
    return unwrap(getByType(type));
  }

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  Result<T> search(TagParams params);

  @POST
  @Path(Paths.SEARCH_SEGMENT)
  @Produces(MediaType.APPLICATION_JSON)
  default List<T> searchRest(TagParams params) {
    return unwrap(search(params));
  }
}
