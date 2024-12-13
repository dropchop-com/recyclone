package com.dropchop.recyclone.mapper.jpa.tagging;

import com.dropchop.recyclone.base.dto.model.tagging.CountryGroup;
import com.dropchop.recyclone.base.dto.model.tagging.LanguageGroup;
import com.dropchop.recyclone.base.dto.model.tagging.Owner;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.*;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaOwner;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaTag;
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
    uses = {ToDtoManipulator.class, DtoPolymorphicFactory.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface TagToDtoMapper extends ToDtoMapper<Tag, JpaTag> {
  @SubclassMapping( source = JpaOwner.class, target = Owner.class)
  @SubclassMapping( source = JpaCountryGroup.class, target = CountryGroup.class)
  @SubclassMapping( source = JpaLanguageGroup.class, target = LanguageGroup.class)
  Tag toDto(JpaTag tags, @Context MappingContext context);
}
