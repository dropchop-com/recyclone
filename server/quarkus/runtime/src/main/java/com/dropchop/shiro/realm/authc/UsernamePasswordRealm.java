package com.dropchop.shiro.realm.authc;

import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
import com.dropchop.recyclone.base.dto.model.security.LoginAccount;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import com.dropchop.shiro.credentials.HashCredentialsMatcher;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class UsernamePasswordRealm extends BaseAuthenticatingRealm {

  private static final Logger log = LoggerFactory.getLogger(UsernamePasswordRealm.class);

  public UsernamePasswordRealm(SecurityLoadingService securityLoadingService) {
    super(securityLoadingService);
  }

  public UsernamePasswordRealm(CacheManager cacheManager, SecurityLoadingService securityLoadingService) {
    super(cacheManager, securityLoadingService);
  }

  public UsernamePasswordRealm(CredentialsMatcher matcher, SecurityLoadingService securityLoadingService) {
    super(matcher, securityLoadingService);
  }

  public UsernamePasswordRealm(CacheManager cacheManager, CredentialsMatcher matcher,
                               SecurityLoadingService securityLoadingService) {
    super(cacheManager, matcher, securityLoadingService);
  }

  @Override
  protected void onInit() {
    super.onInit();
    this.setCredentialsMatcher(new HashCredentialsMatcher());
  }

  protected AuthenticationInfo invokeGetAuthenticationInfo(AuthenticationToken token) {
    if (token instanceof UsernamePasswordToken) {
      String loginName = ((UsernamePasswordToken) token).getUsername();
      User p = this.getSecurityLoadingService().loadUserByUsername(loginName);
      if (p != null && p.getDeactivated() == null) {
        UserAccount account = p.getAccounts().stream()
            .filter(a -> a instanceof LoginAccount)
            .filter(a -> ((LoginAccount)a).getLoginName().equals(loginName))
            .findFirst().orElse(null);
        if (account != null) {
          return new SimpleAuthenticationInfo(p, ((LoginAccount)account).getPassword(), this.getName());
        }
        log.debug("No account found on principal [{}] for login name [{}]", p.getUuid(), loginName);
      }
    }
    throw new AuthenticationException("Unsupported token type [" + token + "]!");
  }
}
