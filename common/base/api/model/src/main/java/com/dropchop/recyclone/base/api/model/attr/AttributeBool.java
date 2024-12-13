package com.dropchop.recyclone.base.api.model.attr;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
public class AttributeBool extends AttributeBase<Boolean> {
  @NonNull
  private Boolean value;

  public AttributeBool(@NonNull String name, @NonNull Boolean value) {
    super(name);
    this.value = value;
  }
}
