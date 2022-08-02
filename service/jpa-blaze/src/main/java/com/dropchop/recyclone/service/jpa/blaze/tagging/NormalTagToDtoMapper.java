package com.dropchop.recyclone.service.jpa.blaze.tagging;

import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.ETag;
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
public interface NormalTagToDtoMapper extends TagToDtoMapper<Tag<TitleTranslation>, ETag> {
}
