package com.dropchop.recyclone.rest.jaxrs.api;

import com.dropchop.recyclone.model.dto.rest.Result;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 12. 21.
 */
@SuppressWarnings("unused")
public interface ClassicModifyResource<T> extends ClassicRestResource<T> {
  Result<T> create(List<T> data);

  default List<T> createRest(List<T> data) {
    return unwrap(create(data));
  }

  Result<T> delete(List<T> data);

  default List<T> deleteRest(List<T> data) {
    return unwrap(delete(data));
  }

  Result<T> update(List<T> data);

  default List<T> updateRest(List<T> data) {
    return unwrap(update(data));
  }
}
