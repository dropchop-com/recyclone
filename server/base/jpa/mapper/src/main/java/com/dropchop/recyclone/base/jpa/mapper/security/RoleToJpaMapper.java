package com.dropchop.recyclone.base.jpa.mapper.security;

import com.dropchop.recyclone.base.dto.model.security.Role;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRole;
import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.ToEntityMapper;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
  uses = EntityFactoryInvoker.class,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  builder = @Builder(disableBuilder = true)
)
public interface RoleToJpaMapper extends ToEntityMapper<Role, JpaRole> {
}
