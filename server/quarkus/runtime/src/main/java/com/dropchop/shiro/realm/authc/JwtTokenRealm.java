package com.dropchop.shiro.realm.authc;

import com.dropchop.shiro.token.JwtShiroToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;

public class JwtTokenRealm extends BaseAuthenticatingRealm {

  @Override
  protected void onInit() {
    super.onInit();
    this.setAuthenticationTokenClass(JwtShiroToken.class);
    this.setCredentialsMatcher(new AllowAllCredentialsMatcher());
  }

  @Override
  protected AuthenticationInfo invokeGetAuthenticationInfo(AuthenticationToken token) {
    if (token instanceof JwtShiroToken) {
      return new SimpleAuthenticationInfo(token.getPrincipal(), token, this.getName());
    }
    throw new AuthenticationException("Unsupported token type [" + token + "]!");
  }
}
