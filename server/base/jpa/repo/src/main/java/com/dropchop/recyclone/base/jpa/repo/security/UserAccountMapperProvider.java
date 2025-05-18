package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.base.api.mapper.AfterToDtoListener;
import com.dropchop.recyclone.base.api.mapper.AfterToEntityListener;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.MappingListener;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.api.repo.mapper.EntityLoadDelegateFactory;
import com.dropchop.recyclone.base.api.repo.mapper.EntityPolymorphicCreateFactory;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.dto.model.security.RoleNodePermissionTemplate;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import com.dropchop.recyclone.base.jpa.mapper.security.RoleNodePermissionToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.RoleNodePermissionToJpaMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.UserAccountToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.UserAccountToJpaMapper;
import com.dropchop.recyclone.base.jpa.model.security.*;
import com.dropchop.recyclone.base.jpa.repo.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@ApplicationScoped
@SuppressWarnings({"unused", "CdiInjectionPointsInspection"})
public class UserAccountMapperProvider extends RecycloneMapperProvider<UserAccount, JpaUserAccount, UUID> {

  @Inject
  UserAccountRepository repository;

  @Inject
  UserAccountToDtoMapper toDtoMapper;

  @Inject
  UserAccountToJpaMapper toEntityMapper;

  @Inject
  MapperSubTypeConfig mapperSubTypeConfig;


  private <X extends Entity> Collection<Class<X>> getSupported() {
    //noinspection unchecked
    return Set.of(
      (Class<X>) JpaTokenAccount.class,
      (Class<X>) JpaLoginAccount.class
    );
  }


  @Override
  public MappingContext getMappingContextForModify() {
    MappingContext context = super.getMappingContextForModify();
    context
      .createWith(
        new EntityPolymorphicCreateFactory<>(getMapperSubTypeConfig(), getSupported())
      );

    MappingListener loadDelegateFactory =
      context.getListeners().stream().filter((ExecContext.Listener l) -> l instanceof EntityLoadDelegateFactory)
        .findFirst().orElse(null);

    if (loadDelegateFactory != null ) {
      ((EntityLoadDelegateFactory)loadDelegateFactory).getSupported().addAll(getSupported());
    }

    return context;
  }
}
