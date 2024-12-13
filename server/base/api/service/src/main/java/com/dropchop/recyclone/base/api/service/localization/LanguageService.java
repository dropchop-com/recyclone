package com.dropchop.recyclone.base.api.service.localization;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.base.api.service.CrudService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
public interface LanguageService extends CrudService<Language> {
  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Localization.LANGUAGE;
  }
}
