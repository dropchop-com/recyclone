package com.dropchop.recyclone.service.jpa.blaze.tagging;

import com.dropchop.recyclone.model.dto.tagging.NamedTag;
import com.dropchop.recyclone.model.entity.jpa.tagging.ENamedTag;
import com.dropchop.recyclone.service.api.mapping.DtoPolymorphicFactory;
import com.dropchop.recyclone.service.api.mapping.FilterDtoMapping;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  uses = {DtoPolymorphicFactory.class, FilterDtoMapping.class},
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  builder = @Builder(disableBuilder = true)
)
public interface NamedTagToDtoMapper extends TagToDtoMapper<NamedTag, ENamedTag> {
}
