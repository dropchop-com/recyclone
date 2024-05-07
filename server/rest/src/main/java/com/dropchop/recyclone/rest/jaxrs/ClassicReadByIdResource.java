package com.dropchop.recyclone.rest.jaxrs;

import com.dropchop.recyclone.model.dto.rest.Result;

import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 12. 21.
 */
public abstract class ClassicReadByIdResource<T, P> extends ClassicReadResource<T, P> {

  public abstract Result<T> getById(UUID id);

  public List<T> getByIdRest(UUID id) {
    return unwrap(getById(id));
  }
}
