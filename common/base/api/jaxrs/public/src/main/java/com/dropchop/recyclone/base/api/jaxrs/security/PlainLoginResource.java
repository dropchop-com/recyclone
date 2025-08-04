package com.dropchop.recyclone.base.api.jaxrs.security;

import com.dropchop.recyclone.base.api.model.rest.DynamicExecContext;
import com.dropchop.recyclone.base.dto.model.invoke.LoginParameters;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.*;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.Security.*;
import static com.dropchop.recyclone.base.api.model.rest.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
@Path(LOGIN_PLAIN)
@DynamicExecContext(LoginParameters.class)
public interface PlainLoginResource {

  @POST
  @Produces({APPLICATION_JSON_DROPCHOP_RESULT})
  Result<User> loginPlain(LoginParameters params);

  @POST
  @Produces({MediaType.APPLICATION_JSON})
  User loginPlainRest(LoginParameters params);
}
