package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.SetEntityDeactivated;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.repo.mapper.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.jpa.mapper.security.UserToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.UserToJpaMapper;
import com.dropchop.recyclone.base.jpa.model.security.JpaUser;
import com.dropchop.recyclone.base.jpa.repo.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.Collection;
import java.util.Set;
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

  private <X extends Entity> Collection<Class<X>> getSupported() {
    //noinspection unchecked
    return Set.of(
        (Class<X>) JpaUser.class
    );
  }

  @Override
  public MappingContext getMappingContextForModify(CommonExecContext<?, ?> sourceContext) {
    Class<?> rootClass = getRepository().getRootClass();
    MappingContext context = super.getMappingContextForModify(sourceContext);
    context
        .createWith(
            new EntityPolymorphicCreateFactory<User, JpaUser>(
                getMapperSubTypeConfig(), getSupported()
            )
        ).afterMapping(
                    new SetEntityDeactivated(rootClass)
            );
    /*context.beforeMapping(new SetAccountUuid());
    context.afterMapping(new SetAccountUser());*/
    return context;
  }
}
