package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.invoke.RoleParams;
import com.dropchop.recyclone.model.dto.security.Role;
import com.dropchop.recyclone.model.entity.jpa.security.ERole;
import com.dropchop.recyclone.service.api.mapping.EntityCreationDelegator;
import com.dropchop.recyclone.service.api.mapping.ToEntityMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
@Mapper(
  componentModel = "cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
  uses = EntityCreationDelegator.class,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RoleToEntityMapper extends ToEntityMapper<Role, RoleParams, ERole> {
}
