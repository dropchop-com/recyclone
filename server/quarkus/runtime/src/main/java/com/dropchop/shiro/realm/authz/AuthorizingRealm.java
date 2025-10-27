package com.dropchop.shiro.realm.authz;

import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
import com.dropchop.recyclone.base.dto.model.invoke.RoleNodeParams;
import com.dropchop.recyclone.base.dto.model.security.Permission;
import com.dropchop.recyclone.base.dto.model.security.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class AuthorizingRealm extends org.apache.shiro.realm.AuthorizingRealm {

  private static final Logger log = LoggerFactory.getLogger(AuthorizingRealm.class);

  private final SecurityLoadingService securityLoadingService;

  public SecurityLoadingService getSecurityLoadingService() {
    return securityLoadingService;
  }

  public AuthorizingRealm(SecurityLoadingService securityLoadingService) {
    this.securityLoadingService = securityLoadingService;
  }

  public AuthorizingRealm(CacheManager cacheManager, SecurityLoadingService securityLoadingService) {
    super(cacheManager);
    this.securityLoadingService = securityLoadingService;
  }

  public AuthorizingRealm(CredentialsMatcher matcher, SecurityLoadingService securityLoadingService) {
    super(matcher);
    this.securityLoadingService = securityLoadingService;
  }

  public AuthorizingRealm(CacheManager cacheManager, CredentialsMatcher matcher,
                          SecurityLoadingService securityLoadingService) {
    super(cacheManager, matcher);
    this.securityLoadingService = securityLoadingService;
  }

  // disable authentication functionalities
  @Override
  public boolean supports(final AuthenticationToken token) {
    return false;
  }

  // disable authentication functionalities
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    return null;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    ProfileTimer timer = new ProfileTimer();
    User p = (User) principals.getPrimaryPrincipal();
    RoleNodeParams params = RoleNodeParams.builder().entity(p.getClass().getSimpleName()).entityId(p.getId()).build();
    params.getFilter().getContent().setTreeLevel(5);
    Collection<Permission> permissions = this.securityLoadingService.loadPermissions(params);
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    if (!permissions.isEmpty()) {
      info.setStringPermissions(permissions.stream().map(Permission::getWildcardString).collect(Collectors.toSet()));
    }
    log.debug("Loaded principal [{}] authorization info permissions in [{}]ms.", p.getId(), timer.stop());
    return info;
  }
}
