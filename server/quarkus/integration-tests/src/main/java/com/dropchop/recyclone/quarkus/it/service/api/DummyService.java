package com.dropchop.recyclone.quarkus.it.service.api;

import com.dropchop.recyclone.quarkus.it.model.api.Constants;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.service.api.CrudService;
import com.dropchop.recyclone.service.api.QueryService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
public interface DummyService extends CrudService<Dummy>, QueryService<Dummy> {
  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Test.DUMMY;
  }

  int delete();
  int deleteByQuery();
}
