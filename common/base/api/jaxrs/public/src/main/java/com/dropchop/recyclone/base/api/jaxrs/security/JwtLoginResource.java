package com.dropchop.recyclone.base.api.jaxrs.security;

import com.dropchop.recyclone.base.api.model.rest.DynamicExecContext;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.AuthorizationRequest;
import com.dropchop.recyclone.base.dto.model.security.AuthorizationResponse;
import com.dropchop.recyclone.base.dto.model.security.OauthLikeRequest;
import com.dropchop.recyclone.base.dto.model.security.OauthLikeResponse;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.Security.LOGIN_JWT;
import static com.dropchop.recyclone.base.api.model.rest.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
@Path(LOGIN_JWT)
@DynamicExecContext(AuthorizationRequest.class)
public interface JwtLoginResource {

  @POST
  @Produces({APPLICATION_JSON_DROPCHOP_RESULT})
  Result<AuthorizationResponse> loginJwt(AuthorizationRequest params);

  @POST
  @Produces({MediaType.APPLICATION_JSON})
  OauthLikeResponse loginJwtRest(OauthLikeRequest params);
}
