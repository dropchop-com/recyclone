package com.dropchop.recyclone.base.jpa.mapper.localization;

import com.dropchop.recyclone.base.api.mapper.DtoPolymorphicFactory;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.base.jpa.mapper.tagging.TagToDtoMapper;
import com.dropchop.recyclone.base.jpa.model.localization.JpaCountry;
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
        ToDtoManipulator.class, DtoPolymorphicFactory.class,
        TagToDtoMapper.class
    }
)
public interface CountryToDtoMapper extends ToDtoMapper<Country, JpaCountry> {
}
