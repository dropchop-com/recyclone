package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.model.entity.jpa.security.EPermissionInstance;
import com.dropchop.recyclone.model.entity.jpa.security.EPermissionTemplate;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@ApplicationScoped
@RepositoryType(RCYN_DEFAULT)
public class PermissionTemplateRepository extends BlazeRepository<EPermissionTemplate, UUID> {

  @Override
  public Class<EPermissionTemplate> getRootClass() {
    return EPermissionTemplate.class;
  }
}
