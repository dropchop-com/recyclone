package com.dropchop.recyclone.base.dto.model.invoke;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@JsonInclude(NON_EMPTY)
public class LoginParameters extends Params {

  private String requestId;

  private String loginName;

  private String password;

  private Boolean rememberMe;

  @Builder.Default
  private Set<String> domainPrefix = new LinkedHashSet<>();

  @Builder.Default
  private Set<Attribute<?>> attributes = new LinkedHashSet<>();
}
