package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.model.entity.jpa.security.EAction;
import com.dropchop.recyclone.repo.jpa.blaze.security.ActionRepository;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.Implementation;
import com.dropchop.recyclone.service.jpa.blaze.CrudServiceImpl;
import com.dropchop.recyclone.service.jpa.blaze.ServiceConfiguration;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.JPA_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@Implementation(JPA_DEFAULT)
public class ActionService extends CrudServiceImpl<Action, CodeParams, EAction, String>
  implements com.dropchop.recyclone.service.api.security.ActionService {

  @Inject
  ActionRepository repository;

  @Inject
  ActionToDtoMapper toDtoMapper;

  @Inject
  ActionToEntityMapper toEntityMapper;

  @Override
  public ServiceConfiguration<Action, CodeParams, EAction, String> getConfiguration(CommonExecContext<CodeParams, Action> execContext) {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper,
      execContext
    );
  }
}
