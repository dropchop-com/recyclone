package com.dropchop.recyclone.base.dto.model.security;

import com.dropchop.recyclone.base.dto.model.invoke.LoginParams;
import com.dropchop.recyclone.base.dto.model.invoke.Params;
import lombok.*;
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
  @Builder.Default
  String grantType = "password";

  @ToString.Include
  String scope;

  @ToString.Include
  String state;

  @ToString.Include
  String username;

  String password;

  Boolean rememberMe;

  public static <R extends AuthorizationRequest> LoginParams toLoginParameters(R req) {
    String scopeStr = req.getScope();
    Set<String> domainPrefixes = new LinkedHashSet<>();
    if (scopeStr != null && !scopeStr.isBlank()) {
      for (String prefix : scopeStr.trim().split(" ")) {
        if (!prefix.isBlank()) {
          domainPrefixes.add(prefix);
        }
      }
    }

    LoginParams loginParams = new LoginParams();
    loginParams.setLoginName(req.getUsername());
    loginParams.setPassword(req.getPassword());
    loginParams.setDomainPrefix(domainPrefixes);
    loginParams.setRememberMe(req.getRememberMe());
    return loginParams;
  }

  public LoginParams toLoginParameters() {
    return toLoginParameters(this);
  }
}
