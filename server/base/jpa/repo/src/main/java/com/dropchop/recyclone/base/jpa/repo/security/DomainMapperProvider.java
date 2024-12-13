package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.base.jpa.mapper.security.DomainToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.DomainToJpaMapper;
import com.dropchop.recyclone.base.dto.model.security.Domain;
import com.dropchop.recyclone.model.entity.jpa.security.JpaDomain;
import com.dropchop.recyclone.base.jpa.repo.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
@SuppressWarnings("unused")
public class DomainMapperProvider extends RecycloneMapperProvider<Domain, JpaDomain, String> {

  @Inject
  DomainRepository repository;

  @Inject
  DomainToDtoMapper toDtoMapper;

  @Inject
  DomainToJpaMapper toEntityMapper;
}
