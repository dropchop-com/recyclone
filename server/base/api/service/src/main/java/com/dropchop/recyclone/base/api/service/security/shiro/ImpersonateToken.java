package com.dropchop.recyclone.base.api.service.security.shiro;

import lombok.Getter;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 22. 6. 26.
 */
public class ImpersonateToken extends UserUuidToken {

  @Getter
  private final String type;
  @Getter
  private final String impersonatorId;

  public ImpersonateToken(String uuid, String type, String impersonatorId) {
    super(uuid);
    this.type = type;
    this.impersonatorId = impersonatorId;
  }
}
