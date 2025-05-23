package com.dropchop.recyclone.base.es.mapper.events;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.ToEntityMapper;
import com.dropchop.recyclone.base.dto.model.event.Event;
import com.dropchop.recyclone.base.es.model.events.EsEvent;
import org.mapstruct.*;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Mapper(
  componentModel = "jakarta-cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
  uses = EntityFactoryInvoker.class,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  builder = @Builder(disableBuilder = true)
)
public interface EventToEsMapper extends ToEntityMapper<Event, EsEvent> {
}
