package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.model.base.Model;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 21. 06. 22.
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
