package com.dropchop.recyclone.rest.api.internal.security;


import com.dropchop.recyclone.model.api.rest.Constants;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.rest.api.DynamicExecContext;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import static com.dropchop.recyclone.model.api.rest.Constants.Paths.Security.PERMISSION_LIST;
import static com.dropchop.recyclone.model.api.security.Constants.Domains.Security.PERMISSION;
import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;
import static com.dropchop.recyclone.rest.api.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;

@Path(Constants.Paths.Security.PERMISSION)
@DynamicExecContext(value = RoleNodeParams.class, internal = true)
@RequiresPermissions(PERMISSION + PERM_DELIM + com.dropchop.recyclone.model.api.security.Constants.Actions.VIEW)
public interface SecurityPermissionsResource {

  @POST
  @Path(PERMISSION_LIST)
  @Produces(APPLICATION_JSON_DROPCHOP_RESULT)
  Result<RoleNodePermission> list(RoleNodeParams params);



}
