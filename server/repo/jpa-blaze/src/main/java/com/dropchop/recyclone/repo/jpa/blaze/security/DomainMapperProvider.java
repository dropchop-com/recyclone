package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.mapper.jpa.security.DomainToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.DomainToJpaMapper;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.model.entity.jpa.security.JpaDomain;
import com.dropchop.recyclone.repo.api.MapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
public class DomainMapperProvider implements MapperProvider<Domain, JpaDomain> {

  @Inject
  DomainToDtoMapper toDtoMapper;

  @Inject
  DomainToJpaMapper toEntityMapper;
}
