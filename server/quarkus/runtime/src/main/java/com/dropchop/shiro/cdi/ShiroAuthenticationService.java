package com.dropchop.shiro.cdi;

import com.dropchop.recyclone.base.api.model.security.PermissionBearer;
import com.dropchop.recyclone.base.api.service.security.AuthenticationService;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@DefaultBean
@ApplicationScoped
public class ShiroAuthenticationService implements AuthenticationService {

  private static final Logger log = LoggerFactory.getLogger(ShiroAuthenticationService.class);
  @Override
  public PermissionBearer getSubject() {
    try {
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
    } catch (UnavailableSecurityManagerException e) {
      log.debug("Security manager not available!");
      return null;
    }
  }

}
