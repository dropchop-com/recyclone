package com.dropchop.recyclone.service.jpa.blaze.localization;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.service.api.mapping.ToEntityMapper;
import com.dropchop.recyclone.service.api.mapping.EntityCreationDelegator;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 2. 02. 22.
 */
@Mapper(
  componentModel = "cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
  uses = EntityCreationDelegator.class,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface LanguageToEntityMapper extends ToEntityMapper<Language, CodeParams, ELanguage> {
}
