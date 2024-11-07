package com.dropchop.recyclone.mapper.jpa.security;

import com.dropchop.recyclone.mapper.api.ToDtoManipulator;
import com.dropchop.recyclone.mapper.api.ToDtoMapper;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRole;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNode;
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
    uses = ToDtoManipulator.class
)
public interface RoleNodeToDtoMapper extends ToDtoMapper<RoleNode, JpaRoleNode> {
}
