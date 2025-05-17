package com.dropchop.recyclone.base.dto.model.invoke;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserAccountParams extends IdentifierParams implements Serializable {

  String loginName;
  String token;
}
