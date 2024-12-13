package com.dropchop.recyclone.base.jpa.mapper.security;

import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUser;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
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
    uses = {ToDtoManipulator.class, UserAccountToDtoMapper.class}
)
public interface UserToDtoMapper extends ToDtoMapper<User, JpaUser> {
}
