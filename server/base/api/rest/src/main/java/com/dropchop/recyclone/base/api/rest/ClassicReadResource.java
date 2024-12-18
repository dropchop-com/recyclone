package com.dropchop.recyclone.base.api.rest;

import com.dropchop.recyclone.base.dto.model.rest.Result;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 12. 21.
 */
public abstract class ClassicReadResource<T, P> implements ClassicRestResource<T> {

  public abstract Result<T> get();

  public List<T> getRest() {
    return unwrap(get());
  }

  public abstract Result<T> search(P parameters);

  public List<T> searchRest(P params) {
    return unwrap(search(params));
  }
}
