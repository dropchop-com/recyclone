package com.dropchop.recyclone.base.jpa.mapper.localization;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.ToEntityMapper;
import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.base.jpa.mapper.tagging.TagToJpaMapper;
import com.dropchop.recyclone.base.jpa.model.localization.JpaLanguage;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 2. 02. 22.
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    builder = @Builder(disableBuilder = true),
    uses = {
        EntityFactoryInvoker.class,
        TagToJpaMapper.class
    }
)
public interface LanguageToJpaMapper extends ToEntityMapper<Language, JpaLanguage> {
}
