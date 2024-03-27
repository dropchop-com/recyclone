package com.dropchop.shiro.realm.authz;

import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.shiro.loaders.SecurityLoaderService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class AuthorizingRealm extends org.apache.shiro.realm.AuthorizingRealm {

  private SecurityLoaderService securityLoaderService;

  //disable authentication functionalities
  @Override
  public boolean supports(final AuthenticationToken token) {
    return false;
  }

  //disable authentication functionalities
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    return null;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    User p = (User) principals.getPrimaryPrincipal();
    List<Permission> permissions = this.securityLoaderService.loadPermissions(p.getClass(), p.getUuid(), null, null);
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    if (!permissions.isEmpty()) {
      info.setStringPermissions(permissions.stream().map(Permission::getWildcardString).collect(Collectors.toSet()));
    }
    return info;
  }

  public SecurityLoaderService getSecurityLoaderService() {
    return securityLoaderService;
  }

  public void setSecurityLoaderService(SecurityLoaderService securityLoaderService) {
    this.securityLoaderService = securityLoaderService;
  }
}
