package com.dropchop.recyclone.base.api.jaxrs.security;

import com.dropchop.recyclone.base.api.model.rest.DynamicExecContext;
import com.dropchop.recyclone.base.dto.model.invoke.LoginParameters;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.User;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.Security.JWT_SEGMENT;
import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.Security.LOGIN;
import static com.dropchop.recyclone.base.api.model.rest.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
@Path(LOGIN)
@DynamicExecContext(LoginParameters.class)
public interface LoginResource {

  @POST
  @Path(JWT_SEGMENT)
  @Produces({APPLICATION_JSON_DROPCHOP_RESULT})
  Result<User> loginJwt(LoginParameters params);

  @POST
  @Path(JWT_SEGMENT)
  @Produces({MediaType.APPLICATION_JSON})
  List<User> loginJwtRest(LoginParameters params);
}
