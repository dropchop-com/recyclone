package com.dropchop.recyclone.mapper.jpa.security;

import com.dropchop.recyclone.mapper.api.EntityFactoryInvoker;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.api.ToEntityMapper;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermissionTemplate;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermissionTemplate;
import org.mapstruct.*;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Mapper(
  componentModel = "jakarta-cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
  uses = EntityFactoryInvoker.class,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  builder = @Builder(disableBuilder = true)
)
public interface RoleNodePermissionToJpaMapper extends ToEntityMapper<RoleNodePermission, JpaRoleNodePermission> {
  @SubclassMapping( source = RoleNodePermission.class, target =  JpaRoleNodePermission.class)
  @SubclassMapping( source = RoleNodePermissionTemplate.class, target = JpaRoleNodePermissionTemplate.class)
  JpaRoleNodePermission toEntity(RoleNodePermission dto, @Context MappingContext context);
}
