package com.dropchop.recyclone.mapper.es.localization;

import com.dropchop.recyclone.mapper.api.EntityFactoryInvoker;
import com.dropchop.recyclone.mapper.api.ToEntityMapper;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.entity.es.localization.EsCountry;
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
public interface CountryToEsMapper extends ToEntityMapper<Country, EsCountry> {
}
