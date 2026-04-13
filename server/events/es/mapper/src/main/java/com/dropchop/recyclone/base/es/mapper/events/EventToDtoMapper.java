package com.dropchop.recyclone.base.es.mapper.events;

import com.dropchop.recyclone.base.api.mapper.DtoPolymorphicFactory;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import com.dropchop.recyclone.base.dto.model.event.Event;
import com.dropchop.recyclone.base.es.model.events.EsEvent;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 2. 02. 22.
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = {
        DtoPolymorphicFactory.class,
        ToDtoManipulator.class
    } //Country.HasTags
)
public interface EventToDtoMapper extends ToDtoMapper<Event, EsEvent> {
  @Override
  @Mapping(target = "id", ignore = true)
  Event toDto(EsEvent model, @Context MappingContext context);
}
