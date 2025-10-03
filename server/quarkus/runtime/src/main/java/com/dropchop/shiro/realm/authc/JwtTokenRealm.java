package com.dropchop.shiro.realm.authc;

import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.shiro.token.JwtShiroToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class JwtTokenRealm extends BaseAuthenticatingRealm {

  private static final Logger log = LoggerFactory.getLogger(JwtTokenRealm.class);

  public JwtTokenRealm(SecurityLoadingService securityLoadingService) {
    super(securityLoadingService);
  }

  public JwtTokenRealm(CacheManager cacheManager, SecurityLoadingService securityLoadingService) {
    super(cacheManager, securityLoadingService);
  }

  public JwtTokenRealm(CredentialsMatcher matcher, SecurityLoadingService securityLoadingService) {
    super(matcher, securityLoadingService);
  }

  public JwtTokenRealm(CacheManager cacheManager, CredentialsMatcher matcher,
                       SecurityLoadingService securityLoadingService) {
    super(cacheManager, matcher, securityLoadingService);
  }

  @Override
  protected void onInit() {
    super.onInit();
    this.setAuthenticationTokenClass(JwtShiroToken.class);
    this.setCredentialsMatcher(new AllowAllCredentialsMatcher());
  }

  @Override
  protected AuthenticationInfo invokeGetAuthenticationInfo(AuthenticationToken token) {
    if (token instanceof JwtShiroToken) {
      Object userObj = token.getPrincipal();
      if (userObj instanceof User tmpuser) {
        Result<User> userResult = this.getSecurityLoadingService().loadValidUserById(tmpuser.getId());
        User user = this.validateResult(tmpuser.getId(), userResult);
        if (user != null) {
          return new SimpleAuthenticationInfo(user, token, this.getName());
        }
      }
    }
    throw new AuthenticationException("Unsupported token type [" + token + "]!");
  }
}
