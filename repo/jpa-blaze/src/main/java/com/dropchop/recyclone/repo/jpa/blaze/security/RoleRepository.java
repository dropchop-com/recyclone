package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.model.entity.jpa.security.ERole;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@ApplicationScoped
public class RoleRepository extends BlazeRepository<ERole, String> {

  @Override
  protected Class<ERole> getRootClass() {
    return ERole.class;
  }
}
