package com.dropchop.recyclone.base.jpa.mapper.security;

import com.dropchop.recyclone.base.api.mapper.DtoPolymorphicFactory;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.jpa.mapper.localization.CountryToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.localization.LanguageToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.tagging.TagToDtoMapper;
import com.dropchop.recyclone.base.jpa.model.security.JpaUser;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 2. 02. 22.
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = {
        ToDtoManipulator.class,
        DtoPolymorphicFactory.class,
        UserAccountToDtoMapper.class,
        CountryToDtoMapper.class,
        LanguageToDtoMapper.class,
        TagToDtoMapper.class,
        PermissionToDtoMapper.class
    }
)
public interface UserToDtoMapper extends ToDtoMapper<User, JpaUser> {
    @Override
    @Mapping(target = "getFirstTagByType", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cloneSimplified", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deactivatedBy", ignore = true)
    User toDto(JpaUser model, @Context MappingContext context);
}
