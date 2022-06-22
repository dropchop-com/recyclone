package com.dropchop.shiro.cdi;

import com.dropchop.recyclone.model.api.security.PermissionBearer;
import com.dropchop.recyclone.service.api.security.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.enterprise.context.ApplicationScoped;


@Slf4j
@ApplicationScoped
public class ShiroAuthenticationService implements AuthenticationService {

  @Override
  public PermissionBearer getSubject() {
    Subject subject = SecurityUtils.getSubject();
    Object principal = subject.getPrincipal();
    if (principal == null) {
      log.warn("Shiro Subject is missing its principal!");
      return null;
    }
    if (principal instanceof PermissionBearer permissionBearer) {
      return permissionBearer;
    }
    log.warn("Shiro Subject principal is not of type [{}]!", PermissionBearer.class);
    return null;
  }

}
