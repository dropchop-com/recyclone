package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.jpa.security.ActionToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.ActionToJpaMapper;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.model.entity.jpa.security.JpaAction;
import com.dropchop.recyclone.repo.api.CrudServiceRepository;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
@RepositoryType(RECYCLONE_JPA_DEFAULT)
public class ActionRepository extends BlazeRepository<JpaAction, String>
    implements CrudServiceRepository<Action, JpaAction, String> {

  Class<JpaAction> rootClass = JpaAction.class;

  @Inject
  ActionToDtoMapper toDtoMapper;

  @Inject
  ActionToJpaMapper toEntityMapper;
}
