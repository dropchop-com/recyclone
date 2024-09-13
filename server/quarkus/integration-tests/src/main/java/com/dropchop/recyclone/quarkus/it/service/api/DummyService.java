package com.dropchop.recyclone.quarkus.it.service.api;

import com.dropchop.recyclone.quarkus.it.model.api.Constants;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.service.api.CrudService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
public interface DummyService extends CrudService<Dummy> {
  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Test.DUMMY;
  }
}
