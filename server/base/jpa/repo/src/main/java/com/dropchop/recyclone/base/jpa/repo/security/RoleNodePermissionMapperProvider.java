package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.base.api.mapper.AfterToDtoListener;
import com.dropchop.recyclone.base.api.mapper.AfterToEntityListener;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.jpa.mapper.security.RoleNodePermissionToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.RoleNodePermissionToJpaMapper;
import com.dropchop.recyclone.base.api.model.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermissionTemplate;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNodePermission;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNodePermissionTemplate;
import com.dropchop.recyclone.base.api.repo.mapper.EntityPolymorphicCreateFactory;
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
@SuppressWarnings({"unused", "CdiInjectionPointsInspection"})
public class RoleNodePermissionMapperProvider extends RecycloneMapperProvider<RoleNodePermission, JpaRoleNodePermission, UUID> {

  @Inject
  RoleNodePermissionRepository repository;

  @Inject
  RoleNodePermissionToDtoMapper toDtoMapper;

  @Inject
  RoleNodePermissionToJpaMapper toEntityMapper;

  @Inject
  MapperSubTypeConfig mapperSubTypeConfig;


  @Override
  public MappingContext getMappingContextForModify() {
    MappingContext context = super.getMappingContextForModify();
    context
        .createWith(
            new EntityPolymorphicCreateFactory<>(getRepository(), getMapperSubTypeConfig())
        ).
        afterMapping(
            (AfterToEntityListener) (model, entity, context1) -> {
              if (model instanceof RoleNodePermissionTemplate p) {
                ((JpaRoleNodePermissionTemplate)entity).setTarget(p.getTarget());
                ((JpaRoleNodePermissionTemplate)entity).setTargetId(p.getTargetId());
              }
            })
        .afterMapping(
            (AfterToDtoListener) (model, dto, context2) -> {
              if (model instanceof JpaRoleNodePermissionTemplate p) {
                ((RoleNodePermissionTemplate)dto).setTarget(p.getTarget());
                ((RoleNodePermissionTemplate)dto).setTargetId(p.getTargetId());
              }
            }
        );
    return context;
  }
}
