package com.dropchop.recyclone.service.api.tagging;

import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.service.api.CrudService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
public interface TagService extends CrudService<Tag> {
  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Tagging.TAG;
  }
}
