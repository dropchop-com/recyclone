package com.dropchop.recyclone.base.api.service.security.shiro;

import lombok.Getter;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 22. 6. 26.
 */
public class ImpersonateToken implements AuthenticationToken {

  private final String uuid;
  @Getter
  private final String type;
  @Getter
  private final String impersonatorId;

  public ImpersonateToken(String uuid, String type, String impersonatorId) {
    this.uuid = uuid;
    this.type = type;
    this.impersonatorId = impersonatorId;
  }


  @Override
  public Object getPrincipal() {
    return this.uuid;
  }

  @Override
  public Object getCredentials() {
    return this.uuid;
  }
}
