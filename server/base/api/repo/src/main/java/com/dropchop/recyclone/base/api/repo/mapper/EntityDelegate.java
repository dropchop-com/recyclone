package com.dropchop.recyclone.base.api.repo.mapper;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 5/15/25.
 */
 @Getter
public abstract class EntityDelegate<D extends Dto, E extends Entity> {

  private final Set<String> onlyForRegisteredActions = new HashSet<>();

  private boolean failIfMissing = true;
  private boolean failIfPresent = false;

  private final Collection<Class<? extends Entity>> supported = new HashSet<>();

  public <X extends Entity> EntityDelegate(Collection<Class<X>> supported) {
    this.supported.addAll(supported);
  }

  public EntityDelegate<D, E> forActionOnly(String action) {
    onlyForRegisteredActions.add(action);
    return this;
  }

  public EntityDelegate<D, E> failIfMissing(boolean failIfMissing) {
    this.failIfMissing = failIfMissing;
    return this;
  }

  public EntityDelegate<D, E> failIfPresent(boolean failIfPresent) {
    this.failIfPresent = failIfPresent;
    return this;
  }

  public boolean isForActions(String action) {
    return onlyForRegisteredActions.isEmpty() || onlyForRegisteredActions.contains(action);
  }

  public <X extends E> boolean supports(Class<X> xClass) {
    if (xClass == null) {
      return false;
    }
    return supported.contains(xClass);
  }
}
