package com.dropchop.recyclone.mapper.jpa.tagging;

import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaTag;
import com.dropchop.recyclone.mapper.api.EntityFactoryInvoker;
import com.dropchop.recyclone.mapper.api.ToEntityMapper;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
  uses = {EntityFactoryInvoker.class},
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  builder = @Builder(disableBuilder = true)
)
public interface TagToJpaMapper extends ToEntityMapper<Tag, JpaTag> {
}
