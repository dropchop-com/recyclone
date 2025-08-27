package com.dropchop.shiro.realm.authc;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

import static com.dropchop.recyclone.base.api.model.security.Constants.*;

@SuppressWarnings("unused")
public class AllowAllViewAuthorizingRealm extends org.apache.shiro.realm.AuthorizingRealm {

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
    Set<String> permissions = Set.of(Domains.ALL + PERM_DELIM + Actions.VIEW);
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    info.setStringPermissions(permissions);
    return info;
  }
}
