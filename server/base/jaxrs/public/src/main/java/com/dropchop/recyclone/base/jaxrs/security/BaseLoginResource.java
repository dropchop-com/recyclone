package com.dropchop.recyclone.base.jaxrs.security;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.invoke.StatusMessage;
import com.dropchop.recyclone.base.api.service.security.AuthenticationService;
import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
import com.dropchop.recyclone.base.api.service.security.shiro.UserUuidToken;
import com.dropchop.recyclone.base.dto.model.security.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import java.util.Collections;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
@Slf4j
public abstract class BaseLoginResource {

  public abstract AuthenticationService getAuthenticationService();

  public abstract SecurityLoadingService getSecurityLoadingService();

  private User loadUserData(Subject subject, Set<String> permissionPrefixes, Set<Attribute<?>> details) {
    Object principal = subject.getPrincipal();
    if (!(principal instanceof User user)) {
      throw new ServiceException(
          new StatusMessage(
              ErrorCode.internal_error,
              String.format("Invalid subject principal type: [%s]!", principal.getClass().getName()),
              details
          )
      );
    }

    user = getSecurityLoadingService().loadUserData(user, permissionPrefixes);
    return user;
  }

  public User login(AuthenticationToken token, Set<String> permissionPrefixes) {
    Set<Attribute<?>> details = Collections.emptySet();
    if (token instanceof UsernamePasswordToken usernamePasswordToken) {
      details = Set.of(
          new AttributeString("username", usernamePasswordToken.getUsername())
      );
    }
    if (token instanceof UserUuidToken userUuidToken) {
      details = Set.of(
          new AttributeString("userid", String.valueOf(userUuidToken.getPrincipal()))
      );
    }
    Subject subject;
    try {
      subject = getAuthenticationService().login(token);
    } catch (AuthenticationException e) {
      throw new ServiceException(
          new StatusMessage(
              ErrorCode.authentication_error, "Invalid credentials!",
              details
          )
      );
    }
    return loadUserData(subject, permissionPrefixes, details);
  }

  public User loadAuthenticated(Set<String> permissionPrefixes) {
    Subject subject = getAuthenticationService().current();
    if (subject == null) {
      throw new ServiceException(
          new StatusMessage(
              ErrorCode.authentication_error, "No authenticated user found!",
              Collections.emptySet()
          )
      );
    }
    if (!subject.isAuthenticated()) {
      throw new ServiceException(
          new StatusMessage(
              ErrorCode.authentication_error, "User is not authenticated!",
              Collections.emptySet()
          )
      );
    }
    return loadUserData(subject, permissionPrefixes, Collections.emptySet());
  }
}
