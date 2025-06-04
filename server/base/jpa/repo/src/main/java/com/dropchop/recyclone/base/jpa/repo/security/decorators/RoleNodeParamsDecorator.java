package com.dropchop.recyclone.base.jpa.repo.security.decorators;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.base.dto.model.invoke.Params;
import com.dropchop.recyclone.base.dto.model.invoke.RoleNodeParams;
import com.dropchop.recyclone.base.api.repo.utils.SearchFields;
import com.dropchop.recyclone.base.jpa.repo.BlazeCriteriaDecorator;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class RoleNodeParamsDecorator<E> extends BlazeCriteriaDecorator<E> {

  @Override
  public void decorate() {
    String alias = getContext().getRootAlias();
    Params params = (Params) getContext().getParams();
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();

    if (params instanceof RoleNodeParams roleNodeParams) {
      if (!roleNodeParams.getIdentifiers().isEmpty()) {
        cb.where(alias + DELIM + SearchFields.Common.UUID)
          .eq(UUID.fromString(roleNodeParams.getIdentifiers().getFirst()));
      } else {
        String target = roleNodeParams.getTarget();
        String targetId = roleNodeParams.getTargetId();
        String entity = roleNodeParams.getEntity();
        String entityId = roleNodeParams.getEntityId();
        Boolean rootOnly = roleNodeParams.getRootOnly();
        Boolean allRoot = roleNodeParams.getAllRoot();
        Boolean allRootWithChildren = roleNodeParams.getAllRootWithChildren();
        if (allRoot != null && allRoot) {
          //to search root nodes only with or without children in the result.
          // Note that it's a flat result
          cb.where(alias + DELIM + "entity").isNull();
          cb.where(alias + DELIM + "entityId").isNull();
          if (target != null && !target.isBlank()) {
            cb.where(alias + DELIM + "target").eq(target);
          }
          if (targetId != null && !targetId.isBlank()) {
            cb.where(alias + DELIM + "targetId").eq(targetId);
          }
          if (allRootWithChildren == null || !allRootWithChildren) {
            cb.where(alias + DELIM + "parent").isNull();
          }
        } else {
          //get role node by entity or target
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
            if (rootOnly != null && rootOnly) {
              cb.where(alias + DELIM + "parent").isNull();
            }
          }
        }
      }
    }
  }
}
