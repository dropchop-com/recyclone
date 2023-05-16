package com.dropchop.recyclone.service.jpa.blaze.tagging;

import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.ENamedTag;
import com.dropchop.recyclone.model.entity.jpa.tagging.ETag;
import com.dropchop.recyclone.service.api.invoke.MappingContext;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
@ApplicationScoped
public class TagToDtoMapperSelector implements TagToDtoMapper<Tag, ETag> {

  @Inject
  NormalTagToDtoMapper normalTagMapper;

  @Inject
  NamedTagToDtoMapper namedTagMapper;

  @Override
  public Tag toDto(ETag entity, MappingContext context) {
    if (entity instanceof ENamedTag e) {
      return namedTagMapper.toDto(e, context);
    }
    return normalTagMapper.toDto(entity, context);
  }
}
