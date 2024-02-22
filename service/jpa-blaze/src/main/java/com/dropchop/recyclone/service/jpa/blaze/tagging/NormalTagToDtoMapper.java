package com.dropchop.recyclone.service.jpa.blaze.tagging;

import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.ETag;
import com.dropchop.recyclone.service.api.mapping.DtoPolymorphicFactory;
import com.dropchop.recyclone.service.api.mapping.ToDtoManipulator;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  uses = {DtoPolymorphicFactory.class, ToDtoManipulator.class},
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  builder = @Builder(disableBuilder = true)
)
public interface NormalTagToDtoMapper extends TagToDtoMapper<Tag, ETag> {
}
