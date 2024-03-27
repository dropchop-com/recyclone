package com.dropchop.shiro.realm.auth;

import com.dropchop.recyclone.model.dto.security.TokenAccount;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.model.dto.security.UserAccount;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unused")
public class TokenRealm extends BaseAuthenticatingRealm {

  private static final Logger log = LoggerFactory.getLogger(TokenRealm.class);

  @Override
  protected void onInit() {
    super.onInit();
    this.setCredentialsMatcher(new SimpleCredentialsMatcher());
    this.setAuthenticationTokenClass(BearerToken.class);
  }


  @Override
  protected AuthenticationInfo invokeGetAuthenticationInfo(AuthenticationToken token) {
    if (token instanceof BearerToken) {
      User p = this.getSecurityLoaderService().loadByToken(String.valueOf(token.getPrincipal()));
      if (p != null && p.getDeactivated() == null) {
        UserAccount account = p.getAccounts().stream().filter(a -> a instanceof TokenAccount).findFirst().orElse(null);
        if (account != null) {
          //TODO: fill principal settings from customer if needed
          return new SimpleAuthenticationInfo(p, token.getPrincipal(), this.getName());
        }
        log.debug("Not Token account found for ");
      }
    }
    throw new AuthenticationException("Unsupported token type [" + token + "]!");
  }

}
