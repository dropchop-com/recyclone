package com.dropchop.recyclone.repo.jpa.blaze.tagging;

import com.dropchop.recyclone.model.entity.jpa.tagging.ETag;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 06. 22.
 */
@ApplicationScoped
@RepositoryType(RCYN_DEFAULT)
public class TagRepository extends BlazeRepository<ETag, UUID> {

  @Override
  public Class<ETag> getRootClass() {
    return ETag.class;
  }
}
