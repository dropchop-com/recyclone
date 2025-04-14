package com.dropchop.recyclone.base.es.mapper.tagging;

import com.dropchop.recyclone.base.api.mapper.DtoPolymorphicFactory;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import com.dropchop.recyclone.base.dto.model.tagging.CountryGroup;
import com.dropchop.recyclone.base.dto.model.tagging.LanguageGroup;
import com.dropchop.recyclone.base.dto.model.tagging.Owner;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.dropchop.recyclone.base.es.model.localization.EsTitleDescriptionTranslation;
import com.dropchop.recyclone.base.es.model.tagging.*;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
@Mapper(
    componentModel = "jakarta-cdi",
    uses = {ToDtoManipulator.class, DtoPolymorphicFactory.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface TagToDtoMapper extends ToDtoMapper<Tag, EsTag> {
  @SubclassMapping( source = EsOwner.class, target = Owner.class)
  @SubclassMapping( source = EsCountryGroup.class, target = CountryGroup.class)
  @SubclassMapping( source = EsLanguageGroup.class, target = LanguageGroup.class)
  Tag toDto(EsBaseTag<EsTag, EsTitleDescriptionTranslation> tag, @Context MappingContext context);
}
