package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.security.UserAccount;
import com.dropchop.recyclone.model.entity.jpa.security.EUserAccount;
import com.dropchop.recyclone.service.api.mapping.DtoPolymorphicFactory;
import com.dropchop.recyclone.service.api.mapping.FilterDtoMapping;
import com.dropchop.recyclone.service.api.mapping.ToDtoMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "jakarta-cdi",
    uses = {DtoPolymorphicFactory.class, FilterDtoMapping.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface UserAccountToDtoMapper extends ToDtoMapper<UserAccount, EUserAccount> {
}
