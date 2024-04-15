package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.model.entity.jpa.security.EPermission;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@ApplicationScoped
@RepositoryType(RECYCLONE_JPA_DEFAULT)
public class PermissionRepository extends BlazeRepository<EPermission, UUID> {

  @Override
  public Class<EPermission> getRootClass() {
    return EPermission.class;
  }
}
