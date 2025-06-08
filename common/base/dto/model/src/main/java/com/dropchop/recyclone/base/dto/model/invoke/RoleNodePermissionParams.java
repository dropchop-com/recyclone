package com.dropchop.recyclone.base.dto.model.invoke;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RoleNodePermissionParams extends IdentifierParams {

  private String roleNodeId;
  private String target;
  private String targetId;

}
