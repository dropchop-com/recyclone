package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.security.Domain;
import com.dropchop.recyclone.base.api.service.CrudService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
public interface DomainService extends CrudService<Domain> {
  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Security.DOMAIN;
  }
}
