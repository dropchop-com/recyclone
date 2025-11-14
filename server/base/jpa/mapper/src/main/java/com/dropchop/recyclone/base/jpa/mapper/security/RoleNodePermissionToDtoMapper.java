package com.dropchop.recyclone.base.jpa.mapper.security;

import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermissionTemplate;
import com.dropchop.recyclone.base.api.mapper.DtoPolymorphicFactory;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNodePermission;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNodePermissionTemplate;
import org.mapstruct.*;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = {
        ToDtoManipulator.class, DtoPolymorphicFactory.class, PermissionToDtoMapper.class,
    }
)
public interface RoleNodePermissionToDtoMapper extends ToDtoMapper<RoleNodePermission, JpaRoleNodePermission> {
  @Override
  @SubclassMapping( source = JpaRoleNodePermissionTemplate.class, target = RoleNodePermissionTemplate.class)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "roleNode", ignore = true)
  @Mapping(target = "type", ignore = true)
  RoleNodePermission toDto(JpaRoleNodePermission node, @Context MappingContext context);
}
