package com.dropchop.recyclone.rest.server;

import com.dropchop.recyclone.model.dto.rest.Result;

import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 12. 21.
 */
public abstract class ClassicReadByIdResource<T, P> extends ClassicReadResource<T, P> {

  public abstract Result<T> getById(UUID id);

  public List<T> getByIdRest(UUID id) {
    return unwrap(getById(id));
  }
}
