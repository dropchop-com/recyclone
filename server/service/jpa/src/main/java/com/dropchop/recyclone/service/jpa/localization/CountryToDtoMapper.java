package com.dropchop.recyclone.service.jpa.localization;

import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.entity.jpa.localization.ECountry;
import com.dropchop.recyclone.service.api.mapping.DtoPolymorphicFactory;
import com.dropchop.recyclone.service.api.mapping.ToDtoManipulator;
import com.dropchop.recyclone.service.api.mapping.ToDtoMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 2. 02. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  uses = {DtoPolymorphicFactory.class, ToDtoManipulator.class}, //Country.HasTags
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  builder = @Builder(disableBuilder = true)
)
public interface CountryToDtoMapper extends ToDtoMapper<Country, ECountry> {
}
