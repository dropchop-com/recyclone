package com.dropchop.recyclone.service.jpa.tagging;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaTag;
import com.dropchop.recyclone.repo.jpa.blaze.tagging.TagMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.tagging.TagRepository;
import com.dropchop.recyclone.service.api.RecycloneType;
import com.dropchop.recyclone.service.api.mapping.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.service.jpa.RecycloneCrudServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * TODO: implement tagging of tags then generalize it and implement logic in CrudServiceImpl
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
public class TagService extends RecycloneCrudServiceImpl<Tag, JpaTag, UUID>
  implements com.dropchop.recyclone.service.api.tagging.TagService {

  @Inject
  TagRepository repository;

  @Inject
  TagMapperProvider mapperProvider;

  @Inject
  MapperSubTypeConfig mapperSubTypeConfig;

  @Override
  protected MappingContext getMappingContextForModify() {
    MappingContext context = super.getMappingContextForModify();
    context
      .createWith(
        new EntityPolymorphicCreateFactory<>(this, mapperSubTypeConfig)
      );
    return context;
  }
}
