package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.model.entity.jpa.security.JpaDomain;
import com.dropchop.recyclone.base.jpa.repo.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
public class DomainRepository extends BlazeRepository<JpaDomain, String> {

  Class<JpaDomain> rootClass = JpaDomain.class;
}
