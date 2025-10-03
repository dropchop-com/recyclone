package com.dropchop.shiro.realm.authc;

import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unused")
public class TokenRealm extends BaseAuthenticatingRealm {

  private static final Logger log = LoggerFactory.getLogger(TokenRealm.class);


  public TokenRealm(SecurityLoadingService securityLoadingService) {
    super(securityLoadingService);
  }


  public TokenRealm(CacheManager cacheManager, SecurityLoadingService securityLoadingService) {
    super(cacheManager, securityLoadingService);
  }


  public TokenRealm(CredentialsMatcher matcher, SecurityLoadingService securityLoadingService) {
    super(matcher, securityLoadingService);
  }


  public TokenRealm(CacheManager cacheManager, CredentialsMatcher matcher,
                    SecurityLoadingService securityLoadingService) {
    super(cacheManager, matcher, securityLoadingService);
  }


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
      Result<User> userResult = this.getSecurityLoadingService().loadValidUserByToken(loginToken);
      User p = this.validateResult(loginToken, userResult);
      if (p != null) {
        return new SimpleAuthenticationInfo(p, token.getPrincipal(), this.getName());
      }
    }
    throw new AuthenticationException("Unsupported token type [" + token + "]!");
  }

}
