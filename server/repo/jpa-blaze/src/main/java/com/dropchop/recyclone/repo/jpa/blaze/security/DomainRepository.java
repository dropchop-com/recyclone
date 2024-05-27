package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.jpa.security.DomainToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.DomainToJpaMapper;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.model.entity.jpa.security.JpaDomain;
import com.dropchop.recyclone.repo.api.CrudServiceRepository;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
@RepositoryType(RECYCLONE_JPA_DEFAULT)
public class DomainRepository extends BlazeRepository<JpaDomain, String>
    implements CrudServiceRepository<Domain, JpaDomain, String> {

  Class<JpaDomain> rootClass = JpaDomain.class;

  @Inject
  DomainToDtoMapper toDtoMapper;

  @Inject
  DomainToJpaMapper toEntityMapper;
}
