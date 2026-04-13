package com.dropchop.recyclone.base.es.mapper.events;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
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
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    builder = @Builder(disableBuilder = true),
    uses = {
        EntityFactoryInvoker.class
    }
)
public interface EventToEsMapper extends ToEntityMapper<Event, EsEvent> {
  @Override
  @Mapping(target = "esAttributes", ignore = true)
  EsEvent toEntity(Event dto, @Context MappingContext context);
}
