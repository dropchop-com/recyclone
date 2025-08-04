package com.dropchop.recyclone.base.dto.model.security;

import com.dropchop.recyclone.base.dto.model.invoke.Params;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class AuthorizationRequest extends Params {

  @ToString.Include
  String clientId;

  String clientSecret;

  @ToString.Include
  String redirectUri;

  @ToString.Include
  String grantType = "password";

  @ToString.Include
  String scope;

  @ToString.Include
  String state;

  @ToString.Include
  String username;

  String password;
}
