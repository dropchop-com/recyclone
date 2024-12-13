package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.dto.model.rest.Result;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 03. 22.
 */
public interface QueryService<D extends Dto> extends Service {
  Result<D> query();
}
