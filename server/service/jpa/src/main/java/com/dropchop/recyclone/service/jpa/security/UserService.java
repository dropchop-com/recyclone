package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUser;
import com.dropchop.recyclone.repo.jpa.blaze.security.UserMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.UserRepository;
import com.dropchop.recyclone.service.api.RecycloneType;
import com.dropchop.recyclone.service.api.mapping.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.service.jpa.RecycloneCrudServiceImpl;
import com.dropchop.recyclone.service.jpa.mapping.SetAccountUser;
import com.dropchop.recyclone.service.jpa.mapping.SetAccountUuid;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;

@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
public class UserService extends RecycloneCrudServiceImpl<User, JpaUser, UUID>
    implements com.dropchop.recyclone.service.api.security.UserService {

  @Inject
  UserRepository repository;

  @Inject
  UserMapperProvider mapperProvider;

  @Inject
  MapperSubTypeConfig mapperSubTypeConfig;

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
