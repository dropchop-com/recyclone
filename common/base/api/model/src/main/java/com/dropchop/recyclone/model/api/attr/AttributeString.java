package com.dropchop.recyclone.model.api.attr;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
public class AttributeString extends AttributeBase<String> {
  @NonNull
  private String value;

  public AttributeString(@NonNull String name, @NonNull String value) {
    super(name);
    this.value = value;
  }
}
