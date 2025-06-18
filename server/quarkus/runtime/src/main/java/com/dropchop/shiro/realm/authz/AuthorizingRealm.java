package com.dropchop.shiro.realm.authz;

import com.dropchop.recyclone.base.dto.model.invoke.RoleNodeParams;
import com.dropchop.recyclone.base.dto.model.security.Permission;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.api.service.security.SecurityLoadingService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class AuthorizingRealm extends org.apache.shiro.realm.AuthorizingRealm {

  //private SecurityLoaderService securityLoaderService;
  private SecurityLoadingService securityLoadingService;


  public SecurityLoadingService getSecurityLoadingService() {
    return securityLoadingService;
  }


  public void setSecurityLoadingService(SecurityLoadingService securityLoadingService) {
    this.securityLoadingService = securityLoadingService;
  }


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
    RoleNodeParams params = RoleNodeParams.builder().entity(p.getClass().getSimpleName()).entityId(p.getId()).build();
    //params.getFilter().getContent().setTreeLevel(5);
    Collection<Permission> permissions = this.securityLoadingService.loadPermissions(params);
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    if (!permissions.isEmpty()) {
      info.setStringPermissions(permissions.stream().map(Permission::getWildcardString).collect(Collectors.toSet()));
    }
    return info;
  }

}
