package com.dropchop.recyclone.service.jpa.blaze.tagging;

import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.ETag;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.tagging.TagRepository;
import com.dropchop.recyclone.service.api.ServiceConfiguration;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.api.invoke.CommonExecContext;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.mapping.EntityDelegateFactory;
import com.dropchop.recyclone.service.api.mapping.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.service.api.mapping.PolymorphicRegistry;
import com.dropchop.recyclone.service.jpa.blaze.RecycloneCrudServiceImpl;
import com.dropchop.recyclone.service.jpa.blaze.localization.LanguageService;
import com.dropchop.recyclone.service.jpa.blaze.mapping.SetLanguage;
import com.dropchop.recyclone.service.jpa.blaze.mapping.SetName;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@ServiceType(RCYN_DEFAULT)
public class TagService extends RecycloneCrudServiceImpl<Tag<TitleTranslation>, ETag, UUID>
  implements com.dropchop.recyclone.service.api.tagging.TagService {

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  TagRepository repository;

  @Inject
  TagToDtoMapper toDtoMapper;

  @Inject
  TagToEntityMapper toEntityMapper;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  PolymorphicRegistry polymorphicRegistry;

  @Override
  public ServiceConfiguration<Tag<TitleTranslation>, ETag, UUID> getConfiguration() {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper
    );
  }

  @Override
  protected MappingContext getMappingContextForModify() {
    MappingContext context = super.getMappingContextForModify();
    context
      .createWith(
        new EntityPolymorphicCreateFactory<>(this, polymorphicRegistry)
      )
      .afterMapping(new SetName());
    return context;
  }
}
