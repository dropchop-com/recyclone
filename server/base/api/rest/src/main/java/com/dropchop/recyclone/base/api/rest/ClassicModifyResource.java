package com.dropchop.recyclone.base.api.rest;

import com.dropchop.recyclone.base.dto.model.rest.Result;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 15. 04. 24.
 */
public abstract class ClassicModifyResource<T> implements ClassicRestResource<T> {
  public abstract Result<T> create(List<T> data);

  public List<T> createRest(List<T> data) {
    return unwrap(create(data));
  }

  public abstract Result<T> delete(List<T> data);

  public List<T> deleteRest(List<T> data) {
    return unwrap(delete(data));
  }

  public abstract Result<T> update(List<T> data);

  public List<T> updateRest(List<T> data) {
    return unwrap(update(data));
  }
}
