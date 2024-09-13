package com.dropchop.shiro.credentials;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14.6.2017.
 */
public class HashCredentialsMatcher extends HashedCredentialsMatcher {


  public HashCredentialsMatcher() {
    this("MD5", 1, true);
  }


  public HashCredentialsMatcher(String hashAlgorithmName) {
    this(hashAlgorithmName, 1, true);
  }


  public HashCredentialsMatcher(String hashAlgorithmName, Integer hashIterations, Boolean store) {
    super(hashAlgorithmName);
    this.setHashIterations(hashIterations);
    this.setStoredCredentialsHexEncoded(store);
  }

  @Override
  public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
    if (token instanceof UsernamePasswordToken) {
      String syspass = String.valueOf(info.getCredentials());
      String providedPass = String.valueOf(((UsernamePasswordToken) token).getPassword());
      return super.doCredentialsMatch(token, info) || syspass.equals(providedPass);
    }
    return false;
  }
}
