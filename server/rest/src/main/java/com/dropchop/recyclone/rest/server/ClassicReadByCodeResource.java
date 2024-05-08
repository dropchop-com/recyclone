package com.dropchop.recyclone.rest.server;

import com.dropchop.recyclone.model.dto.rest.Result;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 12. 21.
 */
public abstract class ClassicReadByCodeResource<T, P> extends ClassicReadResource<T, P> {

  public abstract Result<T> getByCode(String code);

  public List<T> getByCodeRest(String code) {
    return unwrap(getByCode(code));
  }
}
