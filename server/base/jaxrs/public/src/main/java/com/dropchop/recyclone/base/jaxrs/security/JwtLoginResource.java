package com.dropchop.recyclone.base.jaxrs.security;

import com.dropchop.recyclone.base.api.config.JwtConfig;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;
import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.service.security.AuthenticationService;
import com.dropchop.recyclone.base.api.service.security.JwtService;
import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
import com.dropchop.recyclone.base.dto.model.invoke.LoginParameters;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.rest.ResultStatus;
import com.dropchop.recyclone.base.dto.model.security.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 04. 08. 2025
 */
@Getter
@RequestScoped
public class JwtLoginResource extends BaseLoginResource
    implements com.dropchop.recyclone.base.api.jaxrs.security.JwtLoginResource {

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
    claims.put("auth_time", ZonedDateTime.now().toEpochSecond());
    claims.put("iat", ZonedDateTime.now().toEpochSecond());
    ExecContext<?> execContext = execContextContainer.get();
    if (execContext instanceof HasAttributes hasAttributes) {
      String clientAddress = hasAttributes.getAttributeValue(ExecContext.ReqAttributeNames.REQ_CLIENT_ADDRESS);
      if (clientAddress != null) {
        claims.put("client_ip", clientAddress);
      }
    }
    user.getAttributes().forEach(attr -> {
      if (attr.getName().endsWith("accessKey")) {
        claims.put(attr.getName(), attr.getValue());
      }
    });
  }

  private void fillIdClaims(User user, Map<String, Object> claims) {
    claims.put("u", user);
  }

  public <R extends AuthorizationRequest, S extends AuthorizationResponse> void loginJwt(R req, S resp) {
    LoginParameters loginParameters = req.toLoginParameters();

    User user = login(loginParameters);
    User simplifiedUser = user.cloneSimplified();
    resp.setUser(simplifiedUser);

    Map<String, Object> claims = new HashMap<>();
    fillAccessClaims(user, claims);
    // keep the access token simple
    String accessToken = jwtService.encode(jwtConfig, user.getUuid().toString(), claims);
    resp.setAccessToken(accessToken);

    // we respond with id token i.e., all user data
    fillIdClaims(simplifiedUser, claims);
    // add more data to access token
    String idToken = jwtService.encode(jwtConfig, user.getUuid().toString(), claims);
    resp.setIdToken(idToken);

    resp.setCode(req.getRequestId());
    resp.setTokenType("Bearer");
    resp.setExpiresIn(jwtConfig.getTimeoutSeconds());
  }

  @Override
  public Result<AuthorizationResponse> loginJwt(AuthorizationRequest params) {
    ProfileTimer timer = new ProfileTimer();
    params.tryGetResultContentFilter().setTreeLevel(100);
    AuthorizationResponse response = new AuthorizationResponse();
    loginJwt(params, response);
    response.setUser(null);
    Result<AuthorizationResponse> result = new Result<>();
    result.getData().add(response);
    result.setStatus(new ResultStatus(ResultCode.success, timer.mark(), 1));
    return result;
  }

  @Override
  public OauthLikeResponse loginJwtRest(OauthLikeRequest params) {
    OauthLikeResponse response = new OauthLikeResponse();
    try {
      loginJwt(params, response);
      return response;
    } catch (ServiceException e) {
      if (!e.getStatusMessages().isEmpty()) {
        ErrorCode errorCode = e.getStatusMessages().getFirst().getCode();
        String message = e.getStatusMessages().getFirst().getText();
        if (errorCode != null && message != null) {
          response.setError(String.valueOf(errorCode.toString()));
          response.setErrorDescription(message);
        } else if (errorCode != null) {
          response.setError(String.valueOf(errorCode.toString()));
          response.setErrorDescription(e.getMessage());
        } else if (message != null) {
          response.setError(message);
          response.setErrorDescription(e.getMessage());
        } else {
          response.setError(e.getMessage());
        }
      }
    }
    return response;
  }
}
