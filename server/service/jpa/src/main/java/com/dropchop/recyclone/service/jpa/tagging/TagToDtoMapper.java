package com.dropchop.recyclone.service.jpa.tagging;

import com.dropchop.recyclone.model.dto.tagging.CountryGroup;
import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.dropchop.recyclone.model.dto.tagging.Owner;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.*;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaOwner;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaTag;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.mapping.DtoPolymorphicFactory;
import com.dropchop.recyclone.service.api.mapping.ToDtoManipulator;
import com.dropchop.recyclone.service.api.mapping.ToDtoMapper;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
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
