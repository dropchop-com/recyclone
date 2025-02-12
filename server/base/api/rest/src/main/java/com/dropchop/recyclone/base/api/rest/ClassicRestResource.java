package com.dropchop.recyclone.base.api.rest;

import com.dropchop.recyclone.base.dto.model.rest.Result;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 12. 21.
 */
@SuppressWarnings("unused")
public interface ClassicRestResource<T> {
  default List<T> unwrap(Result<T> result) {
    if (result == null) {
      return Collections.emptyList();
    }
    return result.getData();
  }

  default T unwrapFirst(Result<T> result) {
    Collection<T> collection = unwrap(result);
    if (collection.isEmpty()) {
      return null;
    }
    return collection.iterator().next();
  }
}
