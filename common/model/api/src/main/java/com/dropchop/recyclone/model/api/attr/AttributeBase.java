package com.dropchop.recyclone.model.api.attr;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Data
@SuperBuilder
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public abstract class AttributeBase<X> implements Attribute<X> {
  @NonNull
  private String name;

  public String toString() {
    return this.getClass().getSimpleName() + ":" + this.getName();
  }
}
