package com.dropchop.recyclone.base.dto.model.security;

import com.dropchop.recyclone.base.dto.model.invoke.LoginParameters;
import com.dropchop.recyclone.base.dto.model.invoke.Params;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

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

  Boolean rememberMe;

  public static <R extends AuthorizationRequest> LoginParameters toLoginParameters(R req) {
    String scopeStr = req.getScope();
    Set<String> domainPrefixes = new LinkedHashSet<>();
    if (scopeStr != null && !scopeStr.isBlank()) {
      for (String prefix : scopeStr.trim().split(" ")) {
        if (!prefix.isBlank()) {
          domainPrefixes.add(prefix);
        }
      }
    }

    LoginParameters loginParameters = new LoginParameters();
    loginParameters.setLoginName(req.getUsername());
    loginParameters.setPassword(req.getPassword());
    loginParameters.setDomainPrefix(domainPrefixes);
    loginParameters.setRememberMe(req.getRememberMe());
    return loginParameters;
  }

  public LoginParameters toLoginParameters() {
    return toLoginParameters(this);
  }
}
