package com.dropchop.recyclone.service.api.localization;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.service.api.CrudService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
public interface CountryService extends CrudService<Country> {
  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Localization.COUNTRY;
  }
}
