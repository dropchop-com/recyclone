package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.dto.rest.Result;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 03. 22.
 */
public interface CrudService<D extends Dto, P extends Params> extends Service {
  Result<D> search();
  Result<D> create(List<D> dtos);
  Result<D> update(List<D> dtos);
  Result<D> delete(List<D> dtos);
}
