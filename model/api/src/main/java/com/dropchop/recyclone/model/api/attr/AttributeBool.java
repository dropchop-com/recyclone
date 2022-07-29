package com.dropchop.recyclone.model.api.attr;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AttributeBool extends AttributeBase<Boolean> {
  @NonNull
  private Boolean value;

  public AttributeBool(@NonNull String name, @NonNull Boolean value) {
    super(name);
    this.value = value;
  }
}
