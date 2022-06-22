package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.model.api.base.Model;
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
  public boolean isPermitted(String domain, String action) {
    log.warn("Using [{}]", NoopAuthorizationService.class);
    return true;
  }

  @Override
  public boolean isPermitted(String permission) {
    log.warn("Using [{}]", NoopAuthorizationService.class);
    return true;
  }

  @Override
  public <M extends Model> boolean isPermitted(Class<M> subject, String identifier, String domain, String action) {
    log.warn("Using [{}]", NoopAuthorizationService.class);
    return true;
  }

  @Override
  public <M extends Model> boolean isPermitted(Class<M> subject, String identifier, String permission) {
    log.warn("Using [{}]", NoopAuthorizationService.class);
    return true;
  }


}
