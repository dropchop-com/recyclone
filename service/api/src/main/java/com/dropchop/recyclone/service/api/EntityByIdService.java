package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
public interface EntityByIdService<D extends Dto, E extends Entity, ID> extends Service {

  Class<E> getRootClass();

  E findById(D dto);

  E findById(ID id);

  List<E> findById(Collection<ID> ids);

  List<E> findAll();
}
