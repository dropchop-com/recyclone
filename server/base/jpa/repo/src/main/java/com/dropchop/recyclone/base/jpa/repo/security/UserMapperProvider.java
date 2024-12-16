package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.jpa.mapper.security.UserToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.UserToJpaMapper;
import com.dropchop.recyclone.base.api.model.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.jpa.model.security.JpaUser;
import com.dropchop.recyclone.base.api.repo.mapper.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.base.jpa.repo.RecycloneMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.mapping.SetAccountUser;
import com.dropchop.recyclone.base.jpa.repo.mapping.SetAccountUuid;
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
public class UserMapperProvider extends RecycloneMapperProvider<User, JpaUser, UUID> {

  @Inject
  UserRepository repository;

  @Inject
  UserToDtoMapper toDtoMapper;

  @Inject
  UserToJpaMapper toEntityMapper;

  @Inject
  MapperSubTypeConfig mapperSubTypeConfig;

  @Override
  public MappingContext getMappingContextForModify() {
    MappingContext context = super.getMappingContextForModify();
    context
        .createWith(
            new EntityPolymorphicCreateFactory<>(getRepository(), getMapperSubTypeConfig())
        );
    context.beforeMapping(new SetAccountUuid());
    context.afterMapping(new SetAccountUser());
    return context;
  }
}
