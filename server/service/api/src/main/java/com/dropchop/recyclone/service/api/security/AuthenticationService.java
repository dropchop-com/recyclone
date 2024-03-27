package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.model.api.security.PermissionBearer;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 06. 22.
 */
public interface AuthenticationService {

  PermissionBearer getSubject();

}


