package com.dropchop.recyclone.service.jpa.blaze.tagging;

import com.dropchop.recyclone.model.dto.tagging.NamedTag;
import com.dropchop.recyclone.model.entity.jpa.tagging.ENamedTag;
import com.dropchop.recyclone.service.api.mapping.DtoPolymorphicFactory;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
@Mapper(
  componentModel = "cdi",
  uses = {DtoPolymorphicFactory.class},
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface NamedTagToDtoMapper extends TagToDtoMapper<NamedTag, ENamedTag> {
}
