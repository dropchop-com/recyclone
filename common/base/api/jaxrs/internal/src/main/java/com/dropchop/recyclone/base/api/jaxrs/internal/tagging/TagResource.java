package com.dropchop.recyclone.base.api.jaxrs.internal.tagging;

import com.dropchop.recyclone.base.api.model.rest.Constants.Paths;
import com.dropchop.recyclone.base.api.model.security.Constants.Actions;
import com.dropchop.recyclone.base.api.model.security.Constants.Domains;
import com.dropchop.recyclone.base.api.model.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.base.dto.model.invoke.TagParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.dropchop.recyclone.base.api.model.rest.DynamicExecContext;
import com.dropchop.recyclone.base.api.model.rest.MediaType;
import jakarta.ws.rs.*;

import java.util.List;

import static com.dropchop.recyclone.base.api.model.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 01. 22.
 */
@Path(Paths.Tagging.TAG)
@DynamicExecContext(value = TagParams.class, internal = true)
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
