package com.dropchop.recyclone.base.jaxrs.security;

import com.dropchop.recyclone.base.api.config.JwtConfig;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;
import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.rest.ClassicRestResource;
import com.dropchop.recyclone.base.api.service.security.AuthenticationService;
import com.dropchop.recyclone.base.api.service.security.JwtService;
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

import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 04. 08. 2025
 */
@Getter
@RequestScoped
public class JwtLoginResource extends BaseLoginResource
    implements ClassicRestResource<User>, com.dropchop.recyclone.base.api.jaxrs.security.JwtLoginResource {

  public enum Claims {
    auth_time,
    iat,
    client_ip,
    u,
  }

  public static final String USER_ATTR_ACCESS_KEY = "accessKey";
  public static final String USER_ATTR_REMEMBER_ME = "remember_me";
  public static final String USER_ATTR_PERMISSIONS = "permissions";
  public static final String USER_ATTR_ROLES = "roles";
  public static final String USER_ATTR_OWNER = "owner";

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  AuthenticationService authenticationService;

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  SecurityLoadingService securityLoadingService;

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  JwtConfig jwtConfig;

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  ExecContextContainer execContextContainer;

  @Inject
  JwtService jwtService;

  @SuppressWarnings("unused")
  private void fillRefreshClaims(User user, Map<String, Object> claims) {
  }

  private void fillAccessClaims(User user, Map<String, Object> claims) {
    claims.put(Claims.auth_time.name(), ZonedDateTime.now().toEpochSecond());
    claims.put(Claims.iat.name(), ZonedDateTime.now().toEpochSecond());
    ExecContext<?> execContext = execContextContainer.get();
    if (execContext instanceof HasAttributes hasAttributes) {
      String clientAddress = hasAttributes.getAttributeValue(ExecContext.ReqAttributeNames.REQ_CLIENT_ADDRESS);
      if (clientAddress != null) {
        claims.put(Claims.client_ip.name(), clientAddress);
      }
    }
    user.getAttributes().forEach(attr -> {
      if (attr.getName().endsWith(USER_ATTR_ACCESS_KEY)) {
        claims.put(attr.getName(), attr.getValue());
      }
    });
  }

  private void fillIdClaims(User user, Map<String, Object> claims) {
    claims.put(Claims.u.name(), user);
  }

  public <R extends AuthorizationRequest, S extends AuthorizationResponse> void loginJwt(
      R req, S resp, Function<R, User> login) {
    User user = login.apply(req);
    User simplifiedUser = user.cloneSimplified();
    resp.setUser(simplifiedUser);

    Map<String, Object> claims = new HashMap<>();
    fillAccessClaims(simplifiedUser, claims);
    // keep the access token simple
    String accessToken;
    int timeoutSeconds = jwtConfig.getTimeoutSeconds();
    if (user.getAttributeValue(USER_ATTR_REMEMBER_ME, Boolean.FALSE)) {
      timeoutSeconds = jwtConfig.getLongTimeoutSeconds();
    }
    accessToken = jwtService.encode(jwtConfig, timeoutSeconds, user.getUuid().toString(), claims);
    resp.setAccessToken(accessToken);

    // we respond with id token i.e., all user data but,
    // we remove all fields for which simplified attributes are present
    List<String> permissionsInAttributes = user.getAttributeValue(USER_ATTR_PERMISSIONS, new ArrayList<>());
    if (permissionsInAttributes != null && !permissionsInAttributes.isEmpty()) {
      user = user.cloneSimplified(EnumSet.of(User.Fields.permissions));
    }
    List<String> rolesInAttributes = user.getAttributeValue(USER_ATTR_ROLES, new ArrayList<>());
    if (rolesInAttributes != null && !rolesInAttributes.isEmpty()) {
      user = user.cloneSimplified(EnumSet.of(User.Fields.roles));
    }
    Object ownerInAttributes = user.getAttributeValue(USER_ATTR_OWNER, null);
    if (ownerInAttributes != null) {
      user = user.cloneSimplified(EnumSet.of(User.Fields.owner));
    }

    fillIdClaims(user, claims);
    // add more data to access token
    String idToken = jwtService.encode(jwtConfig, timeoutSeconds, user.getUuid().toString(), claims);
    resp.setIdToken(idToken);

    resp.setCode(req.getRequestId());
    resp.setTokenType("Bearer");
    resp.setExpiresIn(timeoutSeconds);
  }

  @Override
  public Result<AuthorizationResponse> loginJwt(AuthorizationRequest params) {
    ProfileTimer timer = new ProfileTimer();
    params.tryGetResultContentFilter().setTreeLevel(100);
    AuthorizationResponse response = new AuthorizationResponse();
    loginJwt(params, response, req -> {
      LoginParams loginParams = req.toLoginParameters();
      AuthenticationToken token = new UsernamePasswordToken(
          loginParams.getLoginName(), loginParams.getPassword(), false
      );
      User user = login(token, loginParams.getDomainPrefix());
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
    loginJwt(params, response, req -> {
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
