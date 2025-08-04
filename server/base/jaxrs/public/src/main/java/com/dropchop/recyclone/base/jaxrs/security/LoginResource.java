package com.dropchop.recyclone.base.jaxrs.security;

import com.dropchop.recyclone.base.api.config.JwtConfig;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.invoke.StatusMessage;
import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.dropchop.recyclone.base.api.rest.ClassicRestResource;
import com.dropchop.recyclone.base.api.service.security.AuthenticationService;
import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
import com.dropchop.recyclone.base.dto.model.security.*;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.rest.ResultStatus;
import com.dropchop.recyclone.base.api.jwt.JwtHelper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
@Slf4j
@RequestScoped
public class LoginResource implements
    com.dropchop.recyclone.base.api.jaxrs.security.LoginResource, ClassicRestResource<User> {

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  AuthenticationService authenticationService;

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  SecurityLoadingService securityLoadingService;

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  JwtConfig jwtConfig;


  @SuppressWarnings("unused")
  private void fillRefreshClaims(User user, Map<String, Object> claims) {
  }

  private <R extends AuthorizationRequest> void fillAccessClaims(R req, Map<String, Object> claims) {
    claims.put("auth_time", ZonedDateTime.now().toEpochSecond());
    claims.put("iat", ZonedDateTime.now().toEpochSecond());
    claims.put("a", "here implement access token");
  }

  private void fillIdClaims(User user, Map<String, Object> claims) {
    User user1 = user.cloneSimplified();
    claims.put("u", user1);
  }

  public <R extends AuthorizationRequest, S extends AuthorizationResponse> void login(R req, S resp) {
    AuthenticationToken token = new UsernamePasswordToken(
        req.getUsername(), req.getPassword(), false
    );
    Subject subject;
    try {
      subject = authenticationService.login(token);
    } catch (AuthenticationException e) {
      throw new ServiceException(
          new StatusMessage(
              ErrorCode.authentication_error, "Invalid credentials!",
              Set.of(
                  new AttributeString("username", req.getUsername())
              )
          )
      );
    }
    Object principal = subject.getPrincipal();
    if (!(principal instanceof User user)) {
      throw new ServiceException(
          new StatusMessage(
              ErrorCode.internal_error,
              String.format("Invalid Shiro Subject principal type: [%s]!", principal.getClass().getName()),
              Set.of(
                  new AttributeString("username", req.getUsername())
              )
          )
      );
    }
    String scopeStr = req.getScope();
    Set<String> domainPrefixes = new LinkedHashSet<>();
    if (scopeStr != null && !scopeStr.isBlank()) {
      for (String prefix : scopeStr.trim().split(" ")) {
        if (!prefix.isBlank()) {
          domainPrefixes.add(prefix);
        }
      }
    }

    log.warn("Invalid credentials provided for user name [{}]!", req.getUsername());

    securityLoadingService.loadUserData(user, domainPrefixes);

    // clear permissions since they are conveniently put in attributes.
    /*user.setPermissions(null);

    // clear sensitive data
    user.getAccounts().forEach(account -> {
      if (account instanceof LoginAccount loginAccount) {
        loginAccount.setPassword(null);
      }
    });

    resp.setUser(user);*/

    Map<String, Object> claims = new HashMap<>();
    fillAccessClaims(req, claims);
    // keep the access token simple
    String accessToken = JwtHelper.encode(jwtConfig, user.getUuid().toString(), claims);
    resp.setAccessToken(accessToken);

    // we respond with id token i.e., all user data
    fillIdClaims(user, claims);
    // add more data to access token
    String idToken = JwtHelper.encode(jwtConfig, user.getUuid().toString(), claims);
    resp.setIdToken(idToken);

    resp.setCode(req.getRequestId());
    resp.setTokenType("Bearer");
    resp.setExpiresIn(jwtConfig.timeoutSeconds);
  }

  @Override
  public Result<AuthorizationResponse> loginJwt(AuthorizationRequest params) {
    params.tryGetResultContentFilter().setTreeLevel(100);

    AuthorizationResponse response = new AuthorizationResponse();
    login(params, response);

    Result<AuthorizationResponse> result = new Result<>();
    result.getData().add(response);
    result.setStatus(new ResultStatus(ResultCode.success, 0, 1, null, null, null));
    return result;
  }

  @Override
  public OauthLikeResponse loginJwtRest(OauthLikeRequest params) {
    OauthLikeResponse response = new OauthLikeResponse();
    try {
      login(params, response);
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
