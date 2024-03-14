package com.dropchop.recyclone.quarkus.it.repo;

import com.dropchop.recyclone.quarkus.it.model.entity.jpa.EDummy;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@ApplicationScoped
@RepositoryType(RCYN_DEFAULT)
public class DummyRepository extends BlazeRepository<EDummy, String> {

  @Override
  public Class<EDummy> getRootClass() {
    return EDummy.class;
  }
}
