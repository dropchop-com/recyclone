package com.dropchop.recyclone.rest.api.internal.security;


import com.dropchop.recyclone.model.api.rest.Constants;
import com.dropchop.recyclone.model.api.security.annotations.RequiresAuthentication;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.model.api.rest.DynamicExecContext;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import static com.dropchop.recyclone.model.api.rest.Constants.Paths.Security.PERMISSIONS_LIST_SEGMENT;
import static com.dropchop.recyclone.model.api.rest.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;

@Path(Constants.Paths.Security.PERMISSIONS)
@DynamicExecContext(value = RoleNodeParams.class, internal = true)
@RequiresAuthentication()
public interface SecurityPermissionsResource {

  @POST
  @Path(PERMISSIONS_LIST_SEGMENT)
  @Produces(APPLICATION_JSON_DROPCHOP_RESULT)
  Result<RoleNodePermission> list(RoleNodeParams params);



}
