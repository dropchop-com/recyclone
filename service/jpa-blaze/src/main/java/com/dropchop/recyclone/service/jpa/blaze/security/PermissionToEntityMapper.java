package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.security.EPermission;
import com.dropchop.recyclone.service.api.mapping.ToEntityMapper;
import com.dropchop.recyclone.service.api.mapping.EntityCreationDelegator;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
@Mapper(
  componentModel = "cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  uses = EntityCreationDelegator.class,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PermissionToEntityMapper extends ToEntityMapper<Permission, CodeParams, EPermission> {
}
