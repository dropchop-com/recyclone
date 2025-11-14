package com.dropchop.recyclone.base.api.mapper;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.dto.model.base.Dto;
import org.mapstruct.Context;
import org.mapstruct.Mapping;

/**
 * Ignore getFirstTagByType.
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 11. 2025.
 */
public interface IgnoreFtbtToDtoMapper<D extends Dto, X extends Model> extends ToDtoMapper<D, X> {
  @Override
  @Mapping(target = "getFirstTagByType", ignore = true)
  D toDto(X model, @Context MappingContext context);
}
