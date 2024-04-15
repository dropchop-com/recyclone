package com.dropchop.recyclone.rest.jaxrs.api.intern.tagging;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.TagParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 01. 22.
 */
@Path(Paths.Tagging.TAG)
@DynamicExecContext(value = TagParams.class, dataClass = Tag.class, internal = true)
@RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.VIEW)
public interface TagResource<T extends Tag> {

  @POST
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.CREATE)
  Result<T> create(List<T> tags);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.CREATE)
  List<T> createRest(List<T> tags);

  @PUT
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.UPDATE)
  Result<T> update(List<T> tags);

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.UPDATE)
  List<T> updateRest(List<T> tags);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON_DROPCHOP_RESULT)
  @RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.DELETE)
  Result<T> delete(List<T> tags);

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @RequiresPermissions(Domains.Tagging.TAG + PERM_DELIM + Actions.DELETE)
  List<T> deleteRest(List<T> tags);
}
