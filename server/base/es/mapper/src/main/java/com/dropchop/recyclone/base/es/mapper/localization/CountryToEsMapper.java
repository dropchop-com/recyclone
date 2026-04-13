package com.dropchop.recyclone.base.es.mapper.localization;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.ToEntityMapper;
import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.base.es.mapper.tagging.TagToEsMapper;
import com.dropchop.recyclone.base.es.model.localization.EsCountry;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 2. 02. 22.
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    builder = @Builder(disableBuilder = true),
    uses = {
        EntityFactoryInvoker.class,
        TitleTranslationToEsMapper.class,
        TagToEsMapper.class
    }
)
public interface CountryToEsMapper extends ToEntityMapper<Country, EsCountry> {
    @Override
    @Mapping(target = "getFirstTagByType", ignore = true)
    @Mapping(target = "language", ignore = true)
    EsCountry toEntity(Country dto, @Context MappingContext context);
}
