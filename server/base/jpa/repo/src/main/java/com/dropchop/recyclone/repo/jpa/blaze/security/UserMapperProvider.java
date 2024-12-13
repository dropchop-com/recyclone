package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.jpa.security.UserToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.UserToJpaMapper;
import com.dropchop.recyclone.base.api.model.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUser;
import com.dropchop.recyclone.repo.api.mapper.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.repo.jpa.blaze.RecycloneMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.mapping.SetAccountUser;
import com.dropchop.recyclone.repo.jpa.blaze.mapping.SetAccountUuid;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
@SuppressWarnings("unused")
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
