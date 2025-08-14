package com.dropchop.recyclone.base.jaxrs.security;

import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.invoke.StatusMessage;
import com.dropchop.recyclone.base.api.rest.ClassicRestResource;
import com.dropchop.recyclone.base.api.service.security.AuthenticationService;
import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
import com.dropchop.recyclone.base.dto.model.invoke.LoginParameters;
import com.dropchop.recyclone.base.dto.model.security.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
@Slf4j
public abstract class BaseLoginResource implements ClassicRestResource<User> {

  public abstract AuthenticationService getAuthenticationService();

  public abstract SecurityLoadingService getSecurityLoadingService();

  protected User login(LoginParameters params) {
    AuthenticationToken token = new UsernamePasswordToken(
        params.getLoginName(), params.getPassword(), false
    );
    Subject subject;
    try {
      subject = getAuthenticationService().login(token);
    } catch (AuthenticationException e) {
      throw new ServiceException(
          new StatusMessage(
              ErrorCode.authentication_error, "Invalid credentials!",
              Set.of(
                  new AttributeString("username", params.getLoginName())
              )
          )
      );
    }
    Object principal = subject.getPrincipal();
    if (!(principal instanceof User user)) {
      throw new ServiceException(
          new StatusMessage(
              ErrorCode.internal_error,
              String.format("Invalid subject principal type: [%s]!", principal.getClass().getName()),
              Set.of(
                  new AttributeString("username", params.getLoginName())
              )
          )
      );
    }
    Set<String> prefixes = params.getDomainPrefix();
    user = getSecurityLoadingService().loadUserData(user, prefixes);
    return user;
  }
}
