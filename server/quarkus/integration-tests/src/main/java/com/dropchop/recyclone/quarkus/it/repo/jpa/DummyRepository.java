package com.dropchop.recyclone.quarkus.it.repo.jpa;

import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaDummy;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@ApplicationScoped
@RepositoryType(RECYCLONE_JPA_DEFAULT)
public class DummyRepository extends BlazeRepository<JpaDummy, String> {

  @Override
  public Class<JpaDummy> getRootClass() {
    return JpaDummy.class;
  }
}
