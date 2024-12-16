package com.dropchop.recyclone.base.api.model.security;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
@SuppressWarnings("unused")
public interface TokenAccount extends UserAccount {
  String getToken();
  void setToken(String token);
}
