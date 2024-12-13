package com.dropchop.recyclone.base.api.model.attr;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 24. 07. 23.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
public class AttributeToRemove extends AttributeBase<Object> {

  private Object value;

  public AttributeToRemove(@NonNull String name) {
    super(name);
  }
}
