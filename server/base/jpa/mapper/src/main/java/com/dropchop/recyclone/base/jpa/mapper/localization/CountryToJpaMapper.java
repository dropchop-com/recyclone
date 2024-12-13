package com.dropchop.recyclone.base.jpa.mapper.localization;

import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaCountry;
import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.ToEntityMapper;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 2. 02. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
  uses = EntityFactoryInvoker.class,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  builder = @Builder(disableBuilder = true)
)
public interface CountryToJpaMapper extends ToEntityMapper<Country, JpaCountry> {
}
