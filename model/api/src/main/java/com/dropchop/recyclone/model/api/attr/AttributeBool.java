package com.dropchop.recyclone.model.api.attr;

import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AttributeBool extends AttributeBase<Boolean> {
  @NonNull
  private Boolean value;

  public AttributeBool(@NonNull String name, @NonNull Boolean value) {
    super(name);
    this.value = value;
  }
}
