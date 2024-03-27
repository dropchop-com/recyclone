package com.dropchop.shiro.realm.auth;

import com.dropchop.shiro.loaders.SecurityLoaderService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
abstract public class BaseAuthenticatingRealm extends AuthenticatingRealm {

  private static final Logger log = LoggerFactory.getLogger(BaseAuthenticatingRealm.class);

  private SecurityLoaderService securityLoaderService;


  public BaseAuthenticatingRealm() {
  }


  public BaseAuthenticatingRealm(CacheManager cacheManager) {
    super(cacheManager);
  }


  public BaseAuthenticatingRealm(CredentialsMatcher matcher) {
    super(matcher);
  }


  public BaseAuthenticatingRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
    super(cacheManager, matcher);
  }


  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) {
    AuthenticationInfo authInfo = this.invokeGetAuthenticationInfo(token);
    log.info("Got principal [{}] authentication info.", token.getPrincipal());
    return authInfo;
  }

  abstract protected AuthenticationInfo invokeGetAuthenticationInfo(AuthenticationToken token);


  public SecurityLoaderService getSecurityLoaderService() {
    return securityLoaderService;
  }

  public void setSecurityLoaderService(SecurityLoaderService securityLoaderService) {
    this.securityLoaderService = securityLoaderService;
  }
}
