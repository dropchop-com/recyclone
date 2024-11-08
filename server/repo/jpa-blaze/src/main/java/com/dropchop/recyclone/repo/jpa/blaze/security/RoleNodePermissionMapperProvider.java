package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.jpa.security.RoleNodePermissionToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.RoleNodePermissionToJpaMapper;
import com.dropchop.recyclone.mapper.jpa.security.RoleNodeToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.RoleNodeToJpaMapper;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermission;
import com.dropchop.recyclone.repo.api.mapper.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.repo.jpa.blaze.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@ApplicationScoped
public class RoleNodePermissionMapperProvider extends RecycloneMapperProvider<RoleNodePermission, JpaRoleNodePermission, String> {

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
        );
    return context;
  }
}
