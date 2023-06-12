package com.dropchop.recyclone.model.dto.security.params;


import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
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
public class SearchPrincipalParameters extends IdentifierParams {

  private String loginName;
  private String token;

}
