package com.dropchop.recyclone.base.api.jaxrs.internal.security;


import com.dropchop.recyclone.base.api.model.rest.Constants;
import com.dropchop.recyclone.base.api.model.security.annotations.RequiresAuthentication;
import com.dropchop.recyclone.base.dto.model.invoke.RoleNodeParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.api.model.rest.DynamicExecContext;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.Security.PERMISSIONS_LIST_SEGMENT;
import static com.dropchop.recyclone.base.api.model.rest.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;

@Path(Constants.Paths.Security.PERMISSIONS)
@DynamicExecContext(value = RoleNodeParams.class, internal = true)
@RequiresAuthentication()
public interface SecurityPermissionsResource {

  @POST
  @Path(PERMISSIONS_LIST_SEGMENT)
  @Produces(APPLICATION_JSON_DROPCHOP_RESULT)
  Result<RoleNodePermission> list(RoleNodeParams params);



}
