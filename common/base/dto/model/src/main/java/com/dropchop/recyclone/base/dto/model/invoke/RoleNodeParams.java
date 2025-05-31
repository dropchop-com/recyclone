package com.dropchop.recyclone.base.dto.model.invoke;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
/*@Getter
@Setter
@NoArgsConstructor
@SuperBuilder*/


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class RoleNodeParams extends IdentifierParams {

  //Defines entity type
  private String entity;

  //Defines entity id;
  private String entityId;

  //Defines target group
  private String target;

  //Defines target group id
  private String targetId;

  //Defines if you want to get back denied permissions.
  private Boolean all;

  //Defines if you want to search only for root permissions
  private Boolean rootOnly;

  public boolean isEmpty() {
    return (this.getIdentifiers() == null || this.getIdentifiers().isEmpty())
        && (this.entity == null || this.entity.isBlank())
        && (this.entityId == null || this.entityId.isBlank())
        && (this.target == null || this.target.isBlank())
        && (this.targetId == null || this.targetId.isBlank());
  }


  public static RoleNodeParams of(RoleNodeParams params) {
    RoleNodeParams result = new RoleNodeParams();
    result.setEntity(params.getEntity());
    result.setEntityId(params.getEntityId());
    result.setTarget(params.getTarget());
    result.setTargetId(params.getTargetId());
    result.setAll(params.getAll());
    result.setRootOnly(params.getRootOnly());
    result.setIdentifiers(params.getIdentifiers());
    return result;
  }
}
