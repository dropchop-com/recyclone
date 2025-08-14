package com.dropchop.recyclone.base.dto.model.invoke;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
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
public class LoginParameters extends Params {

  private String requestId;
  private String loginName;
  private String password;
  private Boolean rememberMe;
  private Set<String> domainPrefix = new LinkedHashSet<>();
  private Set<Attribute<?>> attributes = new LinkedHashSet<>();
}
