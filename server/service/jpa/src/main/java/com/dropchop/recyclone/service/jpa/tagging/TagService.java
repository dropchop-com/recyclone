package com.dropchop.recyclone.service.jpa.tagging;

import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaTag;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.LikeTypeCriteriaDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.tagging.TagRepository;
import com.dropchop.recyclone.service.api.ServiceConfiguration;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.mapping.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.service.jpa.RecycloneCrudServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;


/**
 * TODO: implement tagging of tags then generalize it and implement logic in CrudServiceImpl
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@ServiceType(RECYCLONE_JPA_DEFAULT)
public class TagService extends RecycloneCrudServiceImpl<Tag, JpaTag, UUID>
  implements com.dropchop.recyclone.service.api.tagging.TagService {

  @Inject
  @RepositoryType(RECYCLONE_JPA_DEFAULT)
  TagRepository repository;

  @Inject
  TagToDtoMapper toDtoMapper;

  @Inject
  TagToEntityMapper toEntityMapper;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  MapperSubTypeConfig mapperSubTypeConfig;

  @Override
  public ServiceConfiguration<Tag, JpaTag, UUID> getConfiguration() {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper
    );
  }

  @Override
  protected Collection<CriteriaDecorator> getCommonCriteriaDecorators() {
    Collection<CriteriaDecorator> commonDecorators = super.getCommonCriteriaDecorators();
    ArrayList<CriteriaDecorator> tagDecorators = new ArrayList<>(commonDecorators);
    tagDecorators.add(new LikeTypeCriteriaDecorator());
    return tagDecorators;
  }

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
