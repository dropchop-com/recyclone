package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.model.security.PermissionBearer;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 06. 22.
 */
public interface AuthenticationService {

  PermissionBearer getSubject();

}


