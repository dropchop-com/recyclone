package com.dropchop.recyclone.model.api.attr;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 24. 07. 23.
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
