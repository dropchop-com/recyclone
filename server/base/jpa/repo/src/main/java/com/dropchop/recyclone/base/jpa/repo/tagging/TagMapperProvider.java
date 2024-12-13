package com.dropchop.recyclone.base.jpa.repo.tagging;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.jpa.mapper.tagging.TagToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.tagging.TagToJpaMapper;
import com.dropchop.recyclone.base.api.model.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaTag;
import com.dropchop.recyclone.base.api.repo.mapper.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.base.jpa.repo.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
@SuppressWarnings({"unused", "CdiInjectionPointsInspection"})
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
