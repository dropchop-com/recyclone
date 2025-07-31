package com.dropchop.recyclone.base.api.service.security;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 06. 22.
 */
public interface AuthenticationService {

  Subject login(AuthenticationToken context);

}


