package com.dropchop.recyclone.base.es.mapper.localization;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.es.model.localization.EsTitleDescriptionTranslation;
import org.mapstruct.*;

@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = {
        EntityFactoryInvoker.class
    }
)
public interface TitleDescriptionTranslationToEsMapper {

    @Mapping(target = "language", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    EsTitleDescriptionTranslation toEntity(TitleDescriptionTranslation tdt, @Context MappingContext context);
}
