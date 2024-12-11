package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.model.api.base.Model;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 06. 22.
 */
public interface AuthorizationService {

  boolean isPermitted(String domain, String action);
  boolean isPermitted(String permission);
  <M extends Model> boolean isPermitted(Class<M> subject, String identifier, String domain, String action);
  <M extends Model> boolean isPermitted(Class<M> subject, String identifier, String permission);

}
