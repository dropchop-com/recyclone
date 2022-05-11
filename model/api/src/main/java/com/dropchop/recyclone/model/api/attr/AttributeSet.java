package com.dropchop.recyclone.model.api.attr;

import lombok.*;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 12. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class AttributeSet extends AttributeBase<Set<Attribute<?>>> {
  @NonNull
  private Set<Attribute<?>> value;

  public AttributeSet(@NonNull String name, @NonNull Set<Attribute<?>> value) {
    super(name);
    this.value = value;
  }
}
