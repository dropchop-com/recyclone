package com.dropchop.recyclone.base.jpa.mapper.security;

import com.dropchop.recyclone.base.api.mapper.DtoPolymorphicFactory;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.api.mapper.IgnoreFtbtIdToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.localization.CountryToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.localization.LanguageToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.tagging.TagToDtoMapper;
import com.dropchop.recyclone.base.jpa.model.security.JpaUser;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

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
public interface UserToDtoMapper extends IgnoreFtbtIdToDtoMapper<User, JpaUser> {
}
