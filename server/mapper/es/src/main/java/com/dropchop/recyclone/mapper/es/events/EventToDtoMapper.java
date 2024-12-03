package com.dropchop.recyclone.mapper.es.events;

import com.dropchop.recyclone.mapper.api.DtoPolymorphicFactory;
import com.dropchop.recyclone.mapper.api.ToDtoManipulator;
import com.dropchop.recyclone.mapper.api.ToDtoMapper;
import com.dropchop.recyclone.model.dto.event.Event;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.entity.es.event.EsEvent;
import com.dropchop.recyclone.model.entity.es.localization.EsCountry;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 2. 02. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  uses = {DtoPolymorphicFactory.class, ToDtoManipulator.class}, //Country.HasTags
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  builder = @Builder(disableBuilder = true)
)
public interface EventToDtoMapper extends ToDtoMapper<Event, EsEvent> {
}
