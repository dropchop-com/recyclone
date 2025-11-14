package com.dropchop.recyclone.base.jpa.mapper.security;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.dto.model.security.Permission;
import com.dropchop.recyclone.base.jpa.model.security.JpaPermission;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = {ToDtoManipulator.class}
)
public interface PermissionToDtoMapper extends ToDtoMapper<Permission, JpaPermission> {
  @Override
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "wildcardString", ignore = true)
  Permission toDto(JpaPermission model, @Context MappingContext context);
}
