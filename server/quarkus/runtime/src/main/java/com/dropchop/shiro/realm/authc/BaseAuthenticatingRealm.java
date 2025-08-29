package com.dropchop.shiro.realm.authc;

import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
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

  private final SecurityLoadingService securityLoadingService;


  public SecurityLoadingService getSecurityLoadingService() {
    return securityLoadingService;
  }


  public BaseAuthenticatingRealm(SecurityLoadingService securityLoadingService) {
    this.securityLoadingService = securityLoadingService;
  }

  public BaseAuthenticatingRealm(CacheManager cacheManager, SecurityLoadingService securityLoadingService) {
    super(cacheManager);
    this.securityLoadingService = securityLoadingService;
  }

  public BaseAuthenticatingRealm(CredentialsMatcher matcher, SecurityLoadingService securityLoadingService) {
    super(matcher);
    this.securityLoadingService = securityLoadingService;
  }

  public BaseAuthenticatingRealm(CacheManager cacheManager, CredentialsMatcher matcher,
                                 SecurityLoadingService securityLoadingService) {
    super(cacheManager, matcher);
    this.securityLoadingService = securityLoadingService;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) {
    ProfileTimer timer = new ProfileTimer();
    AuthenticationInfo authInfo = this.invokeGetAuthenticationInfo(token);
    log.trace("Got principal [{}] authentication info in [{}].", token.getPrincipal(), timer.stop());
    return authInfo;
  }

  abstract protected AuthenticationInfo invokeGetAuthenticationInfo(AuthenticationToken token);

}
