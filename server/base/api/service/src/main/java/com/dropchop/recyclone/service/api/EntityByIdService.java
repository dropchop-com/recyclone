package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
public interface EntityByIdService<D extends Dto, E extends Entity, ID> extends ByIdService<E, ID> {

  E findById(D dto);
}
