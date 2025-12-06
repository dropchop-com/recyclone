package com.dropchop.recyclone.base.jpa.service.security;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.dto.model.security.Domain;
import com.dropchop.recyclone.base.jpa.model.security.JpaDomain;
import com.dropchop.recyclone.base.jpa.repo.security.DomainMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.security.DomainRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Getter
@RequestScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class DomainService extends CrudServiceImpl<Domain, JpaDomain, String>
  implements com.dropchop.recyclone.base.api.service.security.DomainService {

  @Inject
  DomainRepository repository;

  @Inject
  DomainMapperProvider mapperProvider;

  @Inject
  CommonExecContext<Domain, ?> executionContext;
}
