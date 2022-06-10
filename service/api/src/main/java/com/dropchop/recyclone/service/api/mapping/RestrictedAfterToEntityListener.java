package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.invoke.Params;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 06. 22.
 */
public abstract class RestrictedAfterToEntityListener<P extends Params> implements AfterToEntityListener<P> {

  final Class<?> onlyForEntity;

  public RestrictedAfterToEntityListener() {
    this.onlyForEntity = null;
  }

  public RestrictedAfterToEntityListener(Class<?> onlyForEntity) {
    this.onlyForEntity = onlyForEntity;
  }
}
