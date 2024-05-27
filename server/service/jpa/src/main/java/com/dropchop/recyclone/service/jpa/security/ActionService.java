package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.model.entity.jpa.security.JpaAction;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.security.ActionRepository;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.jpa.RecycloneCrudServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Getter
@ApplicationScoped
@ServiceType(RECYCLONE_JPA_DEFAULT)
public class ActionService extends RecycloneCrudServiceImpl<Action, JpaAction, String>
  implements com.dropchop.recyclone.service.api.security.ActionService {

  @Inject
  @RepositoryType(RECYCLONE_JPA_DEFAULT)
  ActionRepository repository;
}
