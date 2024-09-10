package com.dropchop.recyclone.repo.jpa.blaze.tagging;

import com.dropchop.recyclone.mapper.jpa.tagging.TagToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.tagging.TagToJpaMapper;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaTag;
import com.dropchop.recyclone.repo.api.MapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
public class TagMapperProvider implements MapperProvider<Tag, JpaTag> {
  @Inject
  TagToDtoMapper toDtoMapper;

  @Inject
  TagToJpaMapper toEntityMapper;
}
