package com.dropchop.recyclone.base.api.service.localization;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.api.service.CrudService;
import com.dropchop.recyclone.base.dto.model.localization.DictionaryTerm;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
public interface DictionaryTermService extends CrudService<DictionaryTerm> {
  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Localization.DICTIONARY_TERM;
  }
}
