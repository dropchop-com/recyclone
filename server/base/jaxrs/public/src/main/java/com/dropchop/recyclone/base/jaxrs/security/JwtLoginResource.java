package com.dropchop.recyclone.base.jaxrs.security;

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
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

import static com.dropchop.recyclone.base.jaxrs.security.JwtLoginDecorator.USER_ATTR_REMEMBER_ME;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 04. 08. 2025
 */
@Getter
@RequestScoped
public class JwtLoginResource extends BaseLoginResource
    implements ClassicRestResource<User>, com.dropchop.recyclone.base.api.jaxrs.security.JwtLoginResource {


  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  AuthenticationService authenticationService;

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  SecurityLoadingService securityLoadingService;

  @Inject
  JwtLoginDecorator jwtLoginDecorator;


  @Override
  public Result<AuthorizationResponse> loginJwt(AuthorizationRequest params) {
    ProfileTimer timer = new ProfileTimer();
    params.tryGetResultContentFilter().setTreeLevel(100);
    AuthorizationResponse response = new AuthorizationResponse();
    jwtLoginDecorator.loginJwt(params, response, req -> {
      LoginParams loginParams = req.toLoginParameters();
      AuthenticationToken token = new UsernamePasswordToken(
          loginParams.getLoginName(), loginParams.getPassword(), false
      );
      User user = this.login(token, loginParams.getDomainPrefix());
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
  public OauthLikeResponse loginJwtRest(OauthLikeRequest params) {
    OauthLikeResponse response = new OauthLikeResponse();
    jwtLoginDecorator.loginJwt(params, response, req -> {
      LoginParams loginParams = req.toLoginParameters();
      AuthenticationToken token = new UsernamePasswordToken(
          loginParams.getLoginName(), loginParams.getPassword(), false
      );
      User user = login(token, loginParams.getDomainPrefix());
      user.setAttributeValue(USER_ATTR_REMEMBER_ME, loginParams.getRememberMe());
      return user;
    });
    return response;
  }
}
