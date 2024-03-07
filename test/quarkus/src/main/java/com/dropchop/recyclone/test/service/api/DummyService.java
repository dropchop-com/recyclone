package com.dropchop.recyclone.test.service.api;

import com.dropchop.recyclone.service.api.CrudService;
import com.dropchop.recyclone.test.model.api.Constants;
import com.dropchop.recyclone.test.model.dto.Dummy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 12. 21.
 */
public interface DummyService extends CrudService<Dummy> {
  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Test.DUMMY;
  }
}
