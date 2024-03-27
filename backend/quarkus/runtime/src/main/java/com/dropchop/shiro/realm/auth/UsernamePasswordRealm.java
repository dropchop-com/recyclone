package com.dropchop.shiro.realm.auth;

import com.dropchop.recyclone.model.dto.security.LoginAccount;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.model.dto.security.UserAccount;
import com.dropchop.shiro.credentials.HashCredentialsMatcher;
import org.apache.shiro.authc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class UsernamePasswordRealm extends BaseAuthenticatingRealm {

  private static final Logger log = LoggerFactory.getLogger(UsernamePasswordRealm.class);

  @Override
  protected void onInit() {
    super.onInit();
    this.setCredentialsMatcher(new HashCredentialsMatcher());
  }

  protected AuthenticationInfo invokeGetAuthenticationInfo(AuthenticationToken token) {
    if (token instanceof UsernamePasswordToken) {
      String loginName = ((UsernamePasswordToken) token).getUsername();
      User p = this.getSecurityLoaderService().loadByLoginName(loginName);
      if (p != null && p.getDeactivated() == null) {
        UserAccount account = p.getAccounts().stream().filter(a -> a instanceof LoginAccount).findFirst().orElse(null);
        if (account != null) {
          return new SimpleAuthenticationInfo(p, ((LoginAccount)account).getPassword(), this.getName());
        }
        log.debug("No user Login account found for [{}]", loginName);
      }
    }
    throw new AuthenticationException("Unsupported token type [" + token + "]!");
  }
}
