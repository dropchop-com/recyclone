package com.dropchop.recyclone.base.jpa.repo.security.decorators;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.base.dto.model.invoke.Params;
import com.dropchop.recyclone.base.dto.model.invoke.RoleNodePermissionParams;
import com.dropchop.recyclone.base.api.repo.utils.SearchFields;
import com.dropchop.recyclone.base.jpa.repo.BlazeCriteriaDecorator;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class RoleNodePermissionParamsDecorator<E> extends BlazeCriteriaDecorator<E> {

  @Override
  public void decorate() {
    String alias = getContext().getRootAlias();
    Params params = (Params)getContext().getParams();
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();

    if (params instanceof RoleNodePermissionParams roleNodePermissionParamsDecorator) {
      cb.where(alias + DELIM + "roleNode" + DELIM + SearchFields.Common.UUID)
        .eq(UUID.fromString(roleNodePermissionParamsDecorator.getRoleNodeId()));
    }
  }
}
