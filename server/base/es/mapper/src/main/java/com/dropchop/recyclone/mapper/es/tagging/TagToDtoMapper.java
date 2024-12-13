package com.dropchop.recyclone.mapper.es.tagging;

import com.dropchop.recyclone.mapper.api.DtoPolymorphicFactory;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.api.ToDtoManipulator;
import com.dropchop.recyclone.mapper.api.ToDtoMapper;
import com.dropchop.recyclone.base.dto.model.tagging.CountryGroup;
import com.dropchop.recyclone.base.dto.model.tagging.LanguageGroup;
import com.dropchop.recyclone.base.dto.model.tagging.Owner;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.dropchop.recyclone.model.entity.es.tagging.EsCountryGroup;
import com.dropchop.recyclone.model.entity.es.tagging.EsLanguageGroup;
import com.dropchop.recyclone.model.entity.es.tagging.EsOwner;
import com.dropchop.recyclone.model.entity.es.tagging.EsTag;
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
  Tag toDto(EsTag tags, @Context MappingContext context);
}
