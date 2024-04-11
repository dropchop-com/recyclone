package com.dropchop.recyclone.rest.jaxrs.api;

import com.dropchop.recyclone.model.dto.rest.Result;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 12. 21.
 */
public interface ClassicReadResource<T> extends ClassicRestResource<T> {

  Result<T> get();

  default List<T> getRest() {
    return unwrap(get());
  }
}
