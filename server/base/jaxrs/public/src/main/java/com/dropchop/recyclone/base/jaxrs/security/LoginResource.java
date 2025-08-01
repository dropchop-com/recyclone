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
import com.dropchop.recyclone.base.dto.model.invoke.LoginParameters;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.rest.ResultStatus;
import com.dropchop.recyclone.base.dto.model.security.LoginAccount;
import com.dropchop.recyclone.base.dto.model.security.Permission;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.api.jwt.JwtHelper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

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
  AuthenticationService service;

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  SecurityLoadingService securityLoadingService;

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  JwtConfig jwtConfig;


  private void fillRequestClaims(User user, Map<String, Object> claims) {
    claims.put("u", user);
    String ownerUuid = user.getAttributeValue("ownerUuid", "");
    if (ownerUuid != null && !ownerUuid.isBlank()) {
      claims.put("o", ownerUuid);
    }
  }

  private void fillFirstResponseClaims(User user, Map<String, Object> claims) {
    String ownerUuid = user.getAttributeValue("ownerUuid", "");
    if (ownerUuid != null && !ownerUuid.isBlank()) {
      claims.put("o", ownerUuid);
    }
    String ownerTitle = user.getAttributeValue("ownerTitle", "");
    if (ownerTitle != null && !ownerTitle.isBlank()) {
      claims.put("on", ownerTitle);
    }
    String ownerTagUuid = user.getAttributeValue("ownerTagUuid", "");
    if (ownerTagUuid != null && !ownerTagUuid.isBlank()) {
      claims.put("ot", ownerTagUuid);
    }
    String shareTagUuid = user.getAttributeValue("shareTagUuid", "");
    if (shareTagUuid != null && !shareTagUuid.isBlank()) {
      claims.put("os", shareTagUuid);
    }
    List<String> roles = user.getAttributeValue("roles", new ArrayList<>());
    if (roles != null && !roles.isEmpty()) {
      claims.put("r", roles);
    }
    List<String> permissions = user.getAttributeValue("permissions", new ArrayList<>());
    if (permissions != null && !permissions.isEmpty()) {
      claims.put("p", permissions);
    }
  }

  @Override
  public Result<User> loginJwt(LoginParameters params) {
    AuthenticationToken token = new UsernamePasswordToken(
        params.getLoginName(), params.getPassword(), false
    );
    Subject subject;
    try {
      subject =  service.login(token);
    } catch (AuthenticationException e) {
      throw new ServiceException(
          new StatusMessage(
              ErrorCode.authentication_error, "Invalid credentials",
              Set.of(
                  new AttributeString("loginName", params.getLoginName())
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
                  new AttributeString("loginName", params.getLoginName())
              )
          )
      );
    }
    log.warn("Invalid credentials provided for login name [{}]!", params.getLoginName());

    // load user with the correct service if it was loaded by the wrong realm.
    // maybe remove this call in the future
    user = securityLoadingService.loadUserById(user.getId());
    Collection<Permission> permissions = securityLoadingService.loadPermissions(user, params.getDomainPrefix());
    user.setPermissions(new LinkedHashSet<>(permissions));

    // put available meta data to user attributes
    securityLoadingService.addMetadata(user);

    // clear permissions since they are conveniently put in attributes.
    user.setPermissions(null);

    // clear sensitive data
    user.getAccounts().forEach(account -> {
      if (account instanceof LoginAccount loginAccount) {
        loginAccount.setPassword(null);
      }
    });

    Map<String, Object> claims = new HashMap<>();
    fillRequestClaims(user, claims);
    String requestJwtToken = JwtHelper.encode(jwtConfig, user.getUuid().toString(), claims);
    user.setAttributeValue("requestJwtToken", requestJwtToken);

    // we respond with triple encoded same data ... Huh, yes, can't win with my argument.
    fillFirstResponseClaims(user, claims);
    String userJwtToken = JwtHelper.encode(jwtConfig, user.getUuid().toString(), claims);
    user.setAttributeValue("userJwtToken", userJwtToken);

    Result<User> result = new Result<>();
    result.getData().add(user);
    result.setStatus(new ResultStatus(ResultCode.success, 0, 1, null, null, null));
    return result;
  }

  @Override
  public List<User> loginJwtRest(LoginParameters params) {
    return unwrap(loginJwt(params));
  }
}
