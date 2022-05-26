package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.model.entity.jpa.security.EAction;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;

import javax.enterprise.context.ApplicationScoped;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@ApplicationScoped
@RepositoryType(RCYN_DEFAULT)
public class ActionRepository extends BlazeRepository<EAction, String> {

  @Override
  public Class<EAction> getRootClass() {
    return EAction.class;
  }
}
