package com.dropchop.recyclone.test.service.jpa.blaze;

import com.dropchop.recyclone.service.api.mapping.EntityFactoryInvoker;
import com.dropchop.recyclone.service.api.mapping.ToEntityMapper;
import com.dropchop.recyclone.test.model.dto.Dummy;
import com.dropchop.recyclone.test.model.entity.jpa.EDummy;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
  uses = EntityFactoryInvoker.class,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  builder = @Builder(disableBuilder = true)
)
public interface DummyToEntityMapper extends ToEntityMapper<Dummy, EDummy> {
}
