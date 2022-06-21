package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.model.api.security.PermissionBearer;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 21. 06. 22.
 */
@Slf4j
@Alternative
@ApplicationScoped
public class NoopAuthorizationService implements AuthorizationService {

  @Override
  public boolean isSubjectPermited(String domain, String action) {
    log.warn("Using [{}]", NoopAuthorizationService.class);
    return true;
  }

  @Override
  public boolean isSubjectPermited(String permission) {
    log.warn("Using [{}]", NoopAuthorizationService.class);
    return true;
  }

  @Override
  public boolean isSubjectPermited(PermissionBearer subject, String domain, String action) {
    log.warn("Using [{}]", NoopAuthorizationService.class);
    return true;
  }

  @Override
  public boolean isSubjectPermited(PermissionBearer subject, String domainAction) {
    log.warn("Using [{}]", NoopAuthorizationService.class);
    return true;
  }

  @Override
  public PermissionBearer getCurrentSubject() {
    log.warn("Using [{}]", NoopAuthorizationService.class);
    return null;
  }
}
