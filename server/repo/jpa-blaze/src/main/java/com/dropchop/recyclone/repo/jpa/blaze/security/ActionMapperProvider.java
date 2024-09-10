package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.jpa.security.ActionToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.ActionToJpaMapper;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.model.entity.jpa.security.JpaAction;
import com.dropchop.recyclone.repo.api.MapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
public class ActionMapperProvider implements MapperProvider<Action, JpaAction> {
  @Inject
  ActionToDtoMapper toDtoMapper;

  @Inject
  ActionToJpaMapper toEntityMapper;
}
