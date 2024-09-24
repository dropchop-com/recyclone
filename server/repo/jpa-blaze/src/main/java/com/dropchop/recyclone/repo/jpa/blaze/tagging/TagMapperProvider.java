package com.dropchop.recyclone.repo.jpa.blaze.tagging;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.jpa.tagging.TagToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.tagging.TagToJpaMapper;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaTag;
import com.dropchop.recyclone.repo.api.mapper.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.repo.jpa.blaze.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
public class TagMapperProvider extends RecycloneMapperProvider<Tag, JpaTag, UUID> {

  @Inject
  TagRepository repository;

  @Inject
  TagToDtoMapper toDtoMapper;

  @Inject
  TagToJpaMapper toEntityMapper;

  @Inject
  MapperSubTypeConfig mapperSubTypeConfig;

  @Override
  public MappingContext getMappingContextForModify() {
    MappingContext context = super.getMappingContextForModify();
    context
        .createWith(
            new EntityPolymorphicCreateFactory<>(getRepository(), getMapperSubTypeConfig())
        );
    return context;
  }
}
