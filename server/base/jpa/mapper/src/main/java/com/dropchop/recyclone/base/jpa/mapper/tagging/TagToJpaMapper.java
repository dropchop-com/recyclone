package com.dropchop.recyclone.base.jpa.mapper.tagging;

import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.dropchop.recyclone.base.jpa.model.tagging.JpaTag;
import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.ToEntityMapper;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    builder = @Builder(disableBuilder = true),
    uses = {EntityFactoryInvoker.class}
)
public interface TagToJpaMapper extends ToEntityMapper<Tag, JpaTag> {
}
