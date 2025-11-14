package com.dropchop.recyclone.base.jpa.mapper.security;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.ToEntityMapper;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.jpa.mapper.localization.CountryToJpaMapper;
import com.dropchop.recyclone.base.jpa.mapper.localization.LanguageToJpaMapper;
import com.dropchop.recyclone.base.jpa.mapper.tagging.TagToJpaMapper;
import com.dropchop.recyclone.base.jpa.model.security.JpaUser;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 2. 02. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
  uses = {
      EntityFactoryInvoker.class,
      TagToJpaMapper.class,
      CountryToJpaMapper.class,
      UserAccountToJpaMapper.class,
      RoleToJpaMapper.class,
      PermissionToJpaMapper.class,
      LanguageToJpaMapper.class
  },
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  builder = @Builder(disableBuilder = true)
)
public interface UserToJpaMapper extends ToEntityMapper<User, JpaUser> {
  @Override
  @Mapping(target = "getFirstTagByType", ignore = true)
  @Mapping(target = "jpaAttributes", ignore = true)
  JpaUser toEntity(User dto, @Context MappingContext context);
}
