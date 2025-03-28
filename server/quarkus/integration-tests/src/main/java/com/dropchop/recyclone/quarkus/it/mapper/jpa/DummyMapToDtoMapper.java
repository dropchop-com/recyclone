package com.dropchop.recyclone.quarkus.it.mapper.jpa;

import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 19. 11. 24
 **/
@SuppressWarnings("unused")
@Mapper(componentModel = "jakarta-cdi")
public interface DummyMapToDtoMapper {

  DummyMapToDtoMapper INSTANCE = Mappers.getMapper(DummyMapToDtoMapper.class);

  Dummy fromMap(Map<String, String> map);

  default ZonedDateTime mapStringToZonedDateTime(String value) {
    return value != null ? ZonedDateTime.parse(value) : null;
  }

  default Set<TitleDescriptionTranslation> mapStringToTitleDescriptionTranslation(String value) {
    return null;
  }
}
