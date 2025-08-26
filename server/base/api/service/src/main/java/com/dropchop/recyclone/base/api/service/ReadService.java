package com.dropchop.recyclone.base.api.service;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.dto.model.rest.Result;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 08. 22.
 */
@SuppressWarnings("unused")
public interface ReadService<D extends Dto> extends Service {
  Result<D> search();
}
