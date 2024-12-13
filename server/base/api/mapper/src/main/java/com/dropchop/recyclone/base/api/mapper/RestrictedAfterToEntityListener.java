package com.dropchop.recyclone.base.api.mapper;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 06. 22.
 */
public abstract class RestrictedAfterToEntityListener implements AfterToEntityListener {

  protected final Class<?> onlyForEntity;

  public RestrictedAfterToEntityListener() {
    this.onlyForEntity = null;
  }

  public RestrictedAfterToEntityListener(Class<?> onlyForEntity) {
    this.onlyForEntity = onlyForEntity;
  }
}
