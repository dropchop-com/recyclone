package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.model.entity.jpa.security.EUser;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.security.UserRepository;
import com.dropchop.recyclone.service.api.ServiceConfiguration;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.mapping.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.service.jpa.RecycloneCrudServiceImpl;
import com.dropchop.recyclone.service.jpa.mapping.SetAccountUser;
import com.dropchop.recyclone.service.jpa.mapping.SetAccountUuid;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

@Slf4j
@ApplicationScoped
@ServiceType(RECYCLONE_JPA_DEFAULT)
public class UserService extends RecycloneCrudServiceImpl<User, EUser, UUID>
    implements com.dropchop.recyclone.service.api.security.UserService {

  @Inject
  @RepositoryType(RECYCLONE_JPA_DEFAULT)
  UserRepository userRepository;

  @Inject
  UserToDtoMapper toDtoMapper;

  @Inject
  UserToEntityMapper toEntityMapper;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  MapperSubTypeConfig mapperSubTypeConfig;

  @Override
  public ServiceConfiguration<User, EUser, UUID> getConfiguration() {
    return new ServiceConfiguration<>(
      userRepository,
      toDtoMapper,
      toEntityMapper
    );
  }


  protected MappingContext getMappingContextForModify() {
    MappingContext context = super.getMappingContextForModify();
    context
      .createWith(
        new EntityPolymorphicCreateFactory<>(this, mapperSubTypeConfig)
      );
    context.beforeMapping(new SetAccountUuid());
    context.afterMapping(new SetAccountUser());
    return context;
  }
}
