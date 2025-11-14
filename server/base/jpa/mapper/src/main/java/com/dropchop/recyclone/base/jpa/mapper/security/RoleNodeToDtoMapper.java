package com.dropchop.recyclone.base.jpa.mapper.security;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import com.dropchop.recyclone.base.dto.model.security.RoleNode;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNode;
import org.mapstruct.*;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = {
        ToDtoManipulator.class,
        RoleNodePermissionToDtoMapper.class
    }
)
public interface RoleNodeToDtoMapper extends ToDtoMapper<RoleNode, JpaRoleNode> {
  @Override
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "roleNodePermissions", ignore = true)
  RoleNode toDto(JpaRoleNode model, @Context MappingContext context);
}
