package com.dropchop.recyclone.model.dto.invoke;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RoleNodePermissionParams extends IdentifierParams {

  private String roleNodeId;

}
