package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.jpa.security.ActionToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.ActionToJpaMapper;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.model.entity.jpa.security.JpaAction;
import com.dropchop.recyclone.repo.jpa.blaze.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
public class ActionMapperProvider extends RecycloneMapperProvider<Action, JpaAction, String> {

  @Inject
  ActionRepository repository;

  @Inject
  ActionToDtoMapper toDtoMapper;

  @Inject
  ActionToJpaMapper toEntityMapper;
}
