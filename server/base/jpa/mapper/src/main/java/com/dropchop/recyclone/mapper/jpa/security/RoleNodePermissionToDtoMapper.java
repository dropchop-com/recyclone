package com.dropchop.recyclone.mapper.jpa.security;

import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermissionTemplate;
import com.dropchop.recyclone.base.api.mapper.DtoPolymorphicFactory;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import com.dropchop.recyclone.model.entity.jpa.security.*;
import org.mapstruct.*;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = {ToDtoManipulator.class, DtoPolymorphicFactory.class}
)
public interface RoleNodePermissionToDtoMapper extends ToDtoMapper<RoleNodePermission, JpaRoleNodePermission> {
  @SubclassMapping( source = JpaRoleNodePermission.class, target = RoleNodePermission.class)
  @SubclassMapping( source = JpaRoleNodePermissionTemplate.class, target = RoleNodePermissionTemplate.class)
  RoleNodePermission toDto(JpaRoleNodePermission node, @Context MappingContext context);
}
