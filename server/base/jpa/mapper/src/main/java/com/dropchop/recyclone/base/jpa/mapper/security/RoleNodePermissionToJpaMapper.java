package com.dropchop.recyclone.base.jpa.mapper.security;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.ToEntityMapper;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermissionTemplate;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNodePermission;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNodePermissionTemplate;
import org.mapstruct.*;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    builder = @Builder(disableBuilder = true),
    uses = {EntityFactoryInvoker.class}
)
public interface RoleNodePermissionToJpaMapper extends ToEntityMapper<RoleNodePermission, JpaRoleNodePermission> {
  @SubclassMapping( source = RoleNodePermissionTemplate.class, target = JpaRoleNodePermissionTemplate.class)
  JpaRoleNodePermission toEntity(RoleNodePermission dto, @Context MappingContext context);
}
