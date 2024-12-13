package com.dropchop.recyclone.rest.server;

import com.dropchop.recyclone.base.dto.model.rest.Result;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 12. 21.
 */
public abstract class ClassicReadByCodeResource<T, P> extends ClassicReadResource<T, P> {

  public abstract Result<T> getByCode(String code);

  public List<T> getByCodeRest(String code) {
    return unwrap(getByCode(code));
  }
}
