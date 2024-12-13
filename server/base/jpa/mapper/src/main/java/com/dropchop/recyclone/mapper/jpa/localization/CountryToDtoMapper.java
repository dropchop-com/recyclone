package com.dropchop.recyclone.mapper.jpa.localization;

import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaCountry;
import com.dropchop.recyclone.mapper.api.DtoPolymorphicFactory;
import com.dropchop.recyclone.mapper.api.ToDtoManipulator;
import com.dropchop.recyclone.mapper.api.ToDtoMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 2. 02. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  uses = {DtoPolymorphicFactory.class, ToDtoManipulator.class}, //Country.HasTags
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  builder = @Builder(disableBuilder = true)
)
public interface CountryToDtoMapper extends ToDtoMapper<Country, JpaCountry> {
}
