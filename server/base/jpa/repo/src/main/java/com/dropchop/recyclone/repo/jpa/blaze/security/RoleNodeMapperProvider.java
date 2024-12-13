package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.api.AfterToDtoListener;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.jpa.security.RoleNodeToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.RoleNodeToJpaMapper;
import com.dropchop.recyclone.base.dto.model.security.RoleNode;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermissionTemplate;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermissionTemplate;
import com.dropchop.recyclone.repo.jpa.blaze.RecycloneMapperProvider;
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
  public MappingContext getMappingContextForRead() {
    MappingContext context = super.getMappingContextForRead();
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
