package com.dropchop.recyclone.model.api.attr;

import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AttributeString extends AttributeBase<String> {
  @NonNull
  private String value;

  public AttributeString(@NonNull String name, @NonNull String value) {
    super(name);
    this.value = value;
  }
}
