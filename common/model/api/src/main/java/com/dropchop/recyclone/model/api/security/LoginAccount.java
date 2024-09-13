package com.dropchop.recyclone.model.api.security;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
@SuppressWarnings("unused")
public interface LoginAccount extends UserAccount {
  String getLoginName();
  void setLoginName(String loginName);

  String getPassword();
  void setPassword(String password);
}
