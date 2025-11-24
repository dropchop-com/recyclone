package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.base.api.mapper.AfterToDtoListener;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.jpa.mapper.security.RoleNodeToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.RoleNodeToJpaMapper;
import com.dropchop.recyclone.base.dto.model.security.RoleNode;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermissionTemplate;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNode;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNodePermissionTemplate;
import com.dropchop.recyclone.base.jpa.repo.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@ApplicationScoped
@SuppressWarnings("unused")
public class RoleNodeMapperProvider extends RecycloneMapperProvider<RoleNode, JpaRoleNode, UUID> {

  @Inject
  RoleNodeRepository repository;

  @Inject
  RoleNodeToDtoMapper toDtoMapper;

  @Inject
  RoleNodeToJpaMapper toEntityMapper;

  @Override
  public MappingContext getMappingContextForRead(CommonExecContext<?, ?> sourceContext) {
    MappingContext context = super.getMappingContextForRead(sourceContext);
    context
        .afterMapping(
            (AfterToDtoListener) (model, dto, context1) -> {
              if (model instanceof JpaRoleNodePermissionTemplate p) {
                ((RoleNodePermissionTemplate)dto).setTarget(p.getTarget());
                ((RoleNodePermissionTemplate)dto).setTargetId(p.getTargetId());
              }
            }
        );
    return context;
  }

}
