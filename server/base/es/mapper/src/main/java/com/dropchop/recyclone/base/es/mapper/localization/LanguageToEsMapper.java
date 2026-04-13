package com.dropchop.recyclone.base.es.mapper.localization;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.IgnoreFtbtLanguageToEntityMapper;
import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.base.es.mapper.tagging.TagToEsMapper;
import com.dropchop.recyclone.base.es.model.localization.EsLanguage;
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
        TitleTranslationToEsMapper.class,
        TagToEsMapper.class
    }
)
public interface LanguageToEsMapper extends IgnoreFtbtLanguageToEntityMapper<Language, EsLanguage> {
}
