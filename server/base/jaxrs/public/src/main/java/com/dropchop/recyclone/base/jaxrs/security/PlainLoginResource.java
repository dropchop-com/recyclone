package com.dropchop.recyclone.base.jaxrs.security;

import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.rest.ClassicRestResource;
import com.dropchop.recyclone.base.api.service.security.AuthenticationService;
import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
import com.dropchop.recyclone.base.dto.model.invoke.LoginParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.rest.ResultStatus;
import com.dropchop.recyclone.base.dto.model.security.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 04. 08. 2025
 */
@Getter
@RequestScoped
public class PlainLoginResource extends BaseLoginResource
    implements ClassicRestResource<User>, com.dropchop.recyclone.base.api.jaxrs.security.PlainLoginResource {

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  AuthenticationService authenticationService;

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  SecurityLoadingService securityLoadingService;

  @Override
  public Result<User> loginPlain(LoginParams params) {
    ProfileTimer timer = new ProfileTimer();
    params.tryGetResultContentFilter().setTreeLevel(100);
    Result<User> result = new Result<>();
    AuthenticationToken token = new UsernamePasswordToken(
        params.getLoginName(), params.getPassword(), false
    );
    User user = login(token, params.getDomainPrefix());
    User simplifiedUser = user.cloneSimplified();
    result.getData().add(simplifiedUser);
    result.setStatus(new ResultStatus(ResultCode.success, timer.mark(), 1));
    return result;
  }

  @Override
  public User loginPlainRest(LoginParams params) {
    return unwrapFirst(loginPlain(params));
  }
}
