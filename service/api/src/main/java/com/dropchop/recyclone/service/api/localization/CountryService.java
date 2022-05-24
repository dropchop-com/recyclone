package com.dropchop.recyclone.service.api.localization;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.entity.jpa.localization.ECountry;
import com.dropchop.recyclone.service.api.CrudService;
import com.dropchop.recyclone.service.api.EntityByIdService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 12. 21.
 */
public interface CountryService extends
  CrudService<Country, CodeParams>,
  EntityByIdService<Country, ECountry, String> {
}
