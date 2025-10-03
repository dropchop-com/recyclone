package com.dropchop.shiro.realm.authc;

import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


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


  protected User validateResult(String loginIdentifier, Result<User> userResult) {
    if (userResult.getStatus().getCode() != ResultCode.success) {
      log.error("Gor error when geting valid user [{}] [{}] [{}]",
        loginIdentifier,
        userResult.getStatus().getMessage().getCode(),
        userResult.getStatus().getMessage().getText()
      );
      throw new AuthenticationException(userResult.getStatus().getMessage().getText());
    }
    List<User> users = userResult.getData();
    if (users.isEmpty()) {
      throw new AuthenticationException("User not found!");
    }
    return users.getFirst();
  }

}
