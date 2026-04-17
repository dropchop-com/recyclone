package com.dropchop.recyclone.base.jaxrs.security;

import com.dropchop.recyclone.base.api.jaxrs.security.JwtTokenRefreshResource;
import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.rest.ClassicRestResource;
import com.dropchop.recyclone.base.api.service.security.AuthenticationService;
import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
import com.dropchop.recyclone.base.dto.model.invoke.LoginParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.rest.ResultStatus;
import com.dropchop.recyclone.base.dto.model.security.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.base.jaxrs.security.JwtLoginDecorator.USER_ATTR_REMEMBER_ME;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 04. 08. 2025
 */
@Getter
@RequestScoped
public class JwtIdTokenResource extends BaseLoginResource
    implements ClassicRestResource<User>, JwtTokenRefreshResource {

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  AuthenticationService authenticationService;

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  SecurityLoadingService securityLoadingService;

  @Inject
  JwtLoginDecorator jwtLoginDecorator;

  @Override
  public Result<AuthorizationResponse> jwtToken(AuthorizationRequest params) {
    ProfileTimer timer = new ProfileTimer();
    params.tryGetResultContentFilter().setTreeLevel(100);
    AuthorizationResponse response = new AuthorizationResponse();
    jwtLoginDecorator.createJwt(params, response, req -> {
      LoginParams loginParams = req.toLoginParameters();
      User user = this.loadAuthenticated(loginParams.getDomainPrefix());
      user.setAttributeValue(USER_ATTR_REMEMBER_ME, loginParams.getRememberMe());
      return user;
    });
    response.setUser(null);
    Result<AuthorizationResponse> result = new Result<>();
    result.getData().add(response);
    result.setStatus(new ResultStatus(ResultCode.success, timer.mark(), 1));
    return result;
  }

  @Override
  public OauthLikeResponse jwtTokenRest(OauthLikeRequest params) {
    OauthLikeResponse response = new OauthLikeResponse();
    jwtLoginDecorator.createJwt(params, response, req -> {
      LoginParams loginParams = req.toLoginParameters();
      User user = this.loadAuthenticated(loginParams.getDomainPrefix());
      user.setAttributeValue(USER_ATTR_REMEMBER_ME, loginParams.getRememberMe());
      return user;
    });
    return response;
  }
}
