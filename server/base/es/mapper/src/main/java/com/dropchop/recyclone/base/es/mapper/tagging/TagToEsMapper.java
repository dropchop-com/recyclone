package com.dropchop.recyclone.base.es.mapper.tagging;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.dropchop.recyclone.base.es.mapper.localization.TitleDescriptionTranslationToEsMapper;
import com.dropchop.recyclone.base.es.model.tagging.EsTag;
import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.ToEntityMapper;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    builder = @Builder(disableBuilder = true),
    uses = {
        EntityFactoryInvoker.class,
        TitleDescriptionTranslationToEsMapper.class
    }
)
public interface TagToEsMapper extends ToEntityMapper<Tag, EsTag> {

    @Override
    @Mapping(target = "getFirstTagByType", ignore = true)
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "esAttributes", ignore = true)
    EsTag toEntity(Tag dto, @Context MappingContext context);
}
