package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.base.dto.model.security.Action;
import com.dropchop.recyclone.model.entity.jpa.security.JpaAction;
import com.dropchop.recyclone.repo.jpa.blaze.security.ActionMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.ActionRepository;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.api.service.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class ActionService extends CrudServiceImpl<Action, JpaAction, String>
  implements com.dropchop.recyclone.base.api.service.security.ActionService {

  @Inject
  ActionRepository repository;

  @Inject
  ActionMapperProvider mapperProvider;
}