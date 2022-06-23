package com.dropchop.recyclone.model.dto.security;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class LoginNamePasswordUserAccount extends UserAccount {

  private String loginName;
  private String password;
}
