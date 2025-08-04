package com.dropchop.recyclone.base.dto.model.security;


import com.dropchop.recyclone.base.dto.model.base.DtoCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class AuthorizationResponse extends DtoCode {

  @JsonInclude(NON_NULL)
  @ToString.Include
  String accessToken;

  @JsonInclude(NON_NULL)
  @ToString.Include
  String refreshToken;

  @JsonInclude(NON_NULL)
  @ToString.Include
  String idToken;

  @JsonInclude(NON_NULL)
  @ToString.Include
  String tokenType;

  @JsonInclude(NON_NULL)
  @ToString.Include
  Integer expiresIn;

  @JsonInclude(NON_NULL)
  User user;
}
