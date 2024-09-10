package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.model.entity.jpa.security.JpaAction;
import com.dropchop.recyclone.repo.jpa.blaze.security.ActionMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.ActionRepository;
import com.dropchop.recyclone.service.api.RecycloneType;
import com.dropchop.recyclone.service.jpa.RecycloneCrudServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
public class ActionService extends RecycloneCrudServiceImpl<Action, JpaAction, String>
  implements com.dropchop.recyclone.service.api.security.ActionService {

  @Inject
  ActionRepository repository;

  @Inject
  ActionMapperProvider mapperProvider;
}
