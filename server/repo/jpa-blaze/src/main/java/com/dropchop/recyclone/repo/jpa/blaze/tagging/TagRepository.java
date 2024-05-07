package com.dropchop.recyclone.repo.jpa.blaze.tagging;

import com.dropchop.recyclone.model.entity.jpa.tagging.JpaTag;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 06. 22.
 */
@ApplicationScoped
@RepositoryType(RECYCLONE_JPA_DEFAULT)
public class TagRepository extends BlazeRepository<JpaTag, UUID> {

  @Override
  public Class<JpaTag> getRootClass() {
    return JpaTag.class;
  }
}
