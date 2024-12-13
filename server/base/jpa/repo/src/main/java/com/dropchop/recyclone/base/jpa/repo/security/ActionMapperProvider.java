package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.base.jpa.mapper.security.ActionToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.ActionToJpaMapper;
import com.dropchop.recyclone.base.dto.model.security.Action;
import com.dropchop.recyclone.model.entity.jpa.security.JpaAction;
import com.dropchop.recyclone.base.jpa.repo.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
@SuppressWarnings("unused")
public class ActionMapperProvider extends RecycloneMapperProvider<Action, JpaAction, String> {

  @Inject
  ActionRepository repository;

  @Inject
  ActionToDtoMapper toDtoMapper;

  @Inject
  ActionToJpaMapper toEntityMapper;
}
