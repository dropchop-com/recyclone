package com.dropchop.shiro.realm.auth;

import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.security.LoginAccount;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.model.dto.security.UserAccount;
import com.dropchop.shiro.credentials.HashCredentialsMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;

@Slf4j
public class UsernamePasswordRealm extends BaseAuthenticatingRealm {


  @Override
  protected void onInit() {
    super.onInit();
    this.setCredentialsMatcher(new HashCredentialsMatcher());
  }

  protected AuthenticationInfo invokeGetAuthenticationInfo(AuthenticationToken token) {
    String loginName = ((UsernamePasswordToken) token).getUsername();
    if (token instanceof UsernamePasswordToken) {
      User<DtoId> p = this.getSecurityLoaderService().loadPrincipalByLoginName(loginName);
      if (p != null && p.getDeactivated() == null) {
        UserAccount account = p.getAccounts().stream().filter(a -> a instanceof LoginAccount).findFirst().orElse(null);
        if (account != null) {
          //TODO: fill principal settings from customer if needed
          return new SimpleAuthenticationInfo(p, ((LoginAccount)account).getPassword(), this.getName());
        }
        log.debug("No user Login account found for [{}]", loginName);
      }
    }
    throw new AuthenticationException("Unsupported token type [" + token + "]!");
  }
}
