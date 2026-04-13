package com.dropchop.recyclone.base.jpa.mapper.localization;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleDescriptionTranslation;
import org.mapstruct.*;

@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = {
        EntityFactoryInvoker.class
    }
)
public interface TitleDescriptionTranslationToJpaMapper {

    @Mapping(target = "language", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    JpaTitleDescriptionTranslation toEntity(TitleDescriptionTranslation tdt, @Context MappingContext context);
}
