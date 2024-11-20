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
public class RoleNodeParams extends IdentifierParams {

  //Defines entity type
  private String entity;

  //Defines entity id;
  private String entityId;

  //Defines target group
  private String target;

  //Defines target group id
  private String targetId;

  //Defines how many levels up on hierarchy
  private Integer maxParentInstanceLevel;

  //Defines if you want to get back denied permissions.
  private Boolean all;

  public boolean isEmpty() {
    return (this.entity == null || this.entity.isBlank())
        && (this.entityId == null || this.entityId.isBlank())
        && (this.target == null || this.target.isBlank())
        && (this.targetId == null || this.targetId.isBlank());
  }

}
