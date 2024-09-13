package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.service.api.CrudService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
public interface DomainService extends CrudService<Domain> {
  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Security.DOMAIN;
  }
}
