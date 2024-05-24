package com.dropchop.recyclone.mapper.api;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 06. 22.
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
