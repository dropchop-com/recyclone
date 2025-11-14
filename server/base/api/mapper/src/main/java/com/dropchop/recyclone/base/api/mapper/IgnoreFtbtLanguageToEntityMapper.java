package com.dropchop.recyclone.base.api.mapper;

import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.dto.model.base.Dto;
import org.mapstruct.Context;
import org.mapstruct.Mapping;

/**
 * Ignore getFirstTagByType.
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 11. 2025.
 */
public interface IgnoreFtbtLanguageToEntityMapper<M extends Dto, E extends Entity> extends ToEntityMapper<M, E> {
  @Override
  @Mapping(target = "getFirstTagByType", ignore = true)
  @Mapping(target = "language", ignore = true)
  E toEntity(M dto, @Context MappingContext context);
}
