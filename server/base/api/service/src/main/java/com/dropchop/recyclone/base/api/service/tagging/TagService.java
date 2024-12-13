package com.dropchop.recyclone.base.api.service.tagging;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.dropchop.recyclone.base.api.service.CrudService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
public interface TagService extends CrudService<Tag> {
  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Tagging.TAG;
  }
}
