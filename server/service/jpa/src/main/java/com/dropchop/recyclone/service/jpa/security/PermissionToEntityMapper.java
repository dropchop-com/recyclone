package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaPermission;
import com.dropchop.recyclone.service.api.mapping.EntityFactoryInvoker;
import com.dropchop.recyclone.service.api.mapping.ToEntityMapper;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
  uses = {EntityFactoryInvoker.class},
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  builder = @Builder(disableBuilder = true)
)
public interface PermissionToEntityMapper extends ToEntityMapper<Permission, JpaPermission> {
}
