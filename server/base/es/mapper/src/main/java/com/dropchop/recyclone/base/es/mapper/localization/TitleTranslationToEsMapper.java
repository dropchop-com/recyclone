package com.dropchop.recyclone.base.es.mapper.localization;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.dto.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.es.model.localization.EsTitleTranslation;
import org.mapstruct.*;

@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = {
        EntityFactoryInvoker.class
    }
)
public interface TitleTranslationToEsMapper {

    @Mapping(target = "language", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    EsTitleTranslation toEntity(TitleTranslation titleTranslation, @Context MappingContext context);
}
