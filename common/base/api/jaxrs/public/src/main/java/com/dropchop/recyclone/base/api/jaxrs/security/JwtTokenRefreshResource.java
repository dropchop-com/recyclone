package com.dropchop.recyclone.base.api.jaxrs.security;

import com.dropchop.recyclone.base.api.model.rest.DynamicExecContext;
import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.api.model.security.annotations.RequiresPermissions;
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
import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.Security.REFRESH_SEGMENT;
import static com.dropchop.recyclone.base.api.model.rest.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;
import static com.dropchop.recyclone.base.api.model.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
@Path(LOGIN_JWT)
@RequiresPermissions(Constants.Domains.Security.USER + PERM_DELIM + Constants.Actions.VIEW)
@DynamicExecContext(AuthorizationRequest.class)
public interface JwtTokenRefreshResource {

  @POST
  @Path(REFRESH_SEGMENT)
  @Produces({APPLICATION_JSON_DROPCHOP_RESULT})
  Result<AuthorizationResponse> jwtToken(AuthorizationRequest params);

  @POST
  @Path(REFRESH_SEGMENT)
  @Produces({MediaType.APPLICATION_JSON})
  OauthLikeResponse jwtTokenRest(OauthLikeRequest params);
}
