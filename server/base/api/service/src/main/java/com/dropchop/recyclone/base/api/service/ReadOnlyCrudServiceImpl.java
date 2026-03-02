package com.dropchop.recyclone.base.api.service;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 03. 22.
 */
@Slf4j
public abstract class ReadOnlyCrudServiceImpl<D extends Dto, E extends Entity, ID>
    extends CrudServiceImpl<D, E, ID>
    implements ReadOnlyService<D> {

  @Override
  public Result<D> create(List<D> dtos) {
    return ReadOnlyService.super.create(dtos);
  }

  @Override
  public Result<D> update(List<D> dtos) {
    return ReadOnlyService.super.update(dtos);
  }

  @Override
  public Result<D> delete(List<D> dtos) {
    return ReadOnlyService.super.delete(dtos);
  }
}
