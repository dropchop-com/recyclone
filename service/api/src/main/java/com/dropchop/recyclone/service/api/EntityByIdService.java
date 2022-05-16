package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;

import java.util.List;
import java.util.Optional;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
public interface EntityByIdService<D extends Dto, E extends Entity, ID> {
  Optional<E> findById(D dto);

  Optional<E> findById(ID id);

  List<E> findById(List<ID> ids);

  List<E> findAll();
}
