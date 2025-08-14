package com.dropchop.shiro.realm.authc;

import com.dropchop.recyclone.base.dto.model.security.TokenAccount;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
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
      String loginToken = String.valueOf(token.getPrincipal());
      User p = this.getSecurityLoadingService().loadUserByToken(loginToken);
      if (p != null && p.getDeactivated() == null) {
        UserAccount account = p.getAccounts().stream()
            .filter(a -> a instanceof TokenAccount)
            .filter(a -> ((TokenAccount)a).getToken().equals(loginToken))
            .findFirst()
            .orElse(null);
        if (account != null) {
          return new SimpleAuthenticationInfo(p, token.getPrincipal(), this.getName());
        }
        log.debug("No account found on principal [{}] for token [{}]", p.getUuid(), loginToken);
      }
    }
    throw new AuthenticationException("Unsupported token type [" + token + "]!");
  }

}
