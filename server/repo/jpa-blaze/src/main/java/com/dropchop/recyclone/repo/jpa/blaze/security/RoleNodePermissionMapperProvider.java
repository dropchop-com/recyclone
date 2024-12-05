package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.api.AfterToDtoListener;
import com.dropchop.recyclone.mapper.api.AfterToEntityListener;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.jpa.security.RoleNodePermissionToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.RoleNodePermissionToJpaMapper;
import com.dropchop.recyclone.mapper.jpa.security.RoleNodeToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.RoleNodeToJpaMapper;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.model.dto.security.RoleNodePermissionTemplate;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermissionTemplate;
import com.dropchop.recyclone.repo.api.mapper.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.repo.jpa.blaze.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.aspectj.lang.annotation.After;

import java.util.UUID;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@ApplicationScoped
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
            new AfterToEntityListener() {
              @Override
              public void after(Model model, Entity entity, MappingContext context) {
                if (model instanceof RoleNodePermissionTemplate p) {
                  ((JpaRoleNodePermissionTemplate)entity).setTarget(p.getTarget());
                  ((JpaRoleNodePermissionTemplate)entity).setTargetId(p.getTargetId());
                }
              }
            })
        .afterMapping(
            new AfterToDtoListener() {
              @Override
              public void after(Model model, Dto dto, MappingContext context) {
                if (model instanceof JpaRoleNodePermissionTemplate p) {
                  ((RoleNodePermissionTemplate)dto).setTarget(p.getTarget());
                  ((RoleNodePermissionTemplate)dto).setTargetId(p.getTargetId());
                }
              }
            }
        );
    return context;
  }
}
