package com.dropchop.recyclone.base.jpa.mapper.tagging;

import com.dropchop.recyclone.base.dto.model.tagging.*;
import com.dropchop.recyclone.base.jpa.model.tagging.*;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.DtoPolymorphicFactory;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = {ToDtoManipulator.class, DtoPolymorphicFactory.class}
)
public interface TagToDtoMapper extends ToDtoMapper<Tag, JpaTag> {
  @SubclassMapping( source = JpaOwner.class, target = Owner.class)
  @SubclassMapping( source = JpaShared.class, target = Shared.class)
  @SubclassMapping( source = JpaCountryGroup.class, target = CountryGroup.class)
  @SubclassMapping( source = JpaLanguageGroup.class, target = LanguageGroup.class)
  Tag toDto(JpaTag tags, @Context MappingContext context);
}
