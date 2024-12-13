package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.base.dto.model.security.Domain;
import com.dropchop.recyclone.model.entity.jpa.security.JpaDomain;
import com.dropchop.recyclone.repo.jpa.blaze.security.DomainMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.DomainRepository;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class DomainService extends CrudServiceImpl<Domain, JpaDomain, String>
  implements com.dropchop.recyclone.service.api.security.DomainService {

  @Inject
  DomainRepository repository;

  @Inject
  DomainMapperProvider mapperProvider;
}
