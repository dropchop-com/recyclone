package com.dropchop.recyclone.base.jpa.mapper.security;

import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import com.dropchop.recyclone.base.dto.model.security.RoleNode;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNode;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

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
}
