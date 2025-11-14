package com.dropchop.recyclone.base.jpa.mapper.localization;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.IgnoreFtbtToEntityMapper;
import com.dropchop.recyclone.base.dto.model.localization.DictionaryTerm;
import com.dropchop.recyclone.base.jpa.mapper.tagging.TagToJpaMapper;
import com.dropchop.recyclone.base.jpa.model.localization.JpaDictionaryTerm;
import org.mapstruct.*;

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
public interface DictionaryTermToJpaMapper extends IgnoreFtbtToEntityMapper<DictionaryTerm, JpaDictionaryTerm> {
}
