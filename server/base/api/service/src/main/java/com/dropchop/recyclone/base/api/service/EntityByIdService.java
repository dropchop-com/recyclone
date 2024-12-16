package com.dropchop.recyclone.base.api.service;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
@SuppressWarnings("unused")
public interface EntityByIdService<D extends Dto, E extends Entity, ID> extends ByIdService<E, ID> {

  E findById(D dto);
}
