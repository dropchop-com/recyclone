package com.dropchop.recyclone.repo.jpa.blaze.security.decorators;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.invoke.Params;
import com.dropchop.recyclone.model.dto.invoke.RoleNodeParams;
import com.dropchop.recyclone.model.dto.invoke.RoleNodePermissionParams;
import com.dropchop.recyclone.repo.api.utils.SearchFields;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeCriteriaDecorator;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class RoleNodeParamsDecorator extends BlazeCriteriaDecorator {


  @Override
  public void decorate() {
    String alias = getContext().getRootAlias();
    Params params = (Params)getContext().getParams();
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();

    if (params instanceof RoleNodeParams roleNodeParams) {
      if (!roleNodeParams.getIdentifiers().isEmpty()) {
        cb.where(alias + DELIM + SearchFields.Common.UUID)
          .eq(UUID.fromString(roleNodeParams.getIdentifiers().get(0)));
      } else {
        String target = roleNodeParams.getTarget();
        String targetId = roleNodeParams.getTargetId();
        String entity = roleNodeParams.getEntity();
        String entityId = roleNodeParams.getEntityId();

        if (entity != null && !entity.isBlank() && entityId != null && !entityId.isBlank()) {
          cb.where(alias + DELIM + "entity").eq(entity);
          cb.where(alias + DELIM + "entityId").eq(entityId);
        } else {
          if (entity == null || entity.isBlank()) {
            cb.where(alias + DELIM + "entity").isNull();
          } else {
            cb.where(alias + DELIM + "entity").eq(entity);
          }
          if (entityId == null || entityId.isBlank()) {
            cb.where(alias + DELIM + "entityId").isNull();
          } else {
            cb.where(alias + DELIM + "entityId").eq(entityId);
          }
          if (target != null && !target.isBlank()) {
            cb.where(alias + DELIM + "target").eq(target);
          } else {
            cb.where(alias + DELIM + "target").isNull();
          }
          if (targetId != null && !targetId.isBlank()) {
            cb.where(alias + DELIM + "targetId").eq(targetId);
          } else {
            cb.where(alias + DELIM + "targetId").isNull();
          }
        }
      }
    }

  }
}