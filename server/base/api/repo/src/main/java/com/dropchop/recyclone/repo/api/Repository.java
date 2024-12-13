package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.base.api.model.base.Model;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 02. 22.
 */
public interface Repository<E extends Model> {

  <S extends E> Class<S> getRootClass();
}
