package com.dropchop.recyclone.base.api.mapper;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.dto.model.base.Dto;
import org.mapstruct.Context;
import org.mapstruct.Mapping;

/**
 * Ignore getFirstTagByType and id.
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 11. 2025.
 */
public interface IgnoreFtbtIdToDtoMapper<D extends Dto, X extends Model> extends ToDtoMapper<D, X> {
  @Override
  @Mapping(target = "getFirstTagByType", ignore = true)
  @Mapping(target = "id", ignore = true)
  D toDto(X model, @Context MappingContext context);
}
