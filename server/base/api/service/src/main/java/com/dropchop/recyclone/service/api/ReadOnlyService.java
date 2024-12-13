package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.dto.model.rest.Result;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 08. 22.
 */
@SuppressWarnings("unused")
public interface ReadOnlyService<D extends Dto> extends CrudService<D> {
  @Override
  default Result<D> create(List<D> dtos) {
    throw new UnsupportedOperationException("Service is read only!");
  }

  @Override
  default Result<D> update(List<D> dtos) {
    throw new UnsupportedOperationException("Service is read only!");
  }

  @Override
  default Result<D> delete(List<D> dtos) {
    throw new UnsupportedOperationException("Service is read only!");
  }
}
