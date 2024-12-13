package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.model.base.Model;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 06. 22.
 */
@SuppressWarnings("unused")
public interface AuthorizationService {

  boolean isPermitted(String domain, String action);
  boolean isPermitted(String permission);
  <M extends Model> boolean isPermitted(Class<M> subject, String identifier, String domain, String action);
  <M extends Model> boolean isPermitted(Class<M> subject, String identifier, String permission);
}
