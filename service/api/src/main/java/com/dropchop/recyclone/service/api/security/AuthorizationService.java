package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.model.api.security.PermissionBearer;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 06. 22.
 */
public interface AuthorizationService {

  boolean isSubjectPermited(String domain, String action);
  boolean isSubjectPermited(String permission);
  boolean isSubjectPermited(PermissionBearer subject, String domain, String action);
  boolean isSubjectPermited(PermissionBearer subject, String domainAction);

  PermissionBearer getCurrentSubject();

}
