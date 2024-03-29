package com.dropchop.recyclone.model.api.attr;

import com.dropchop.recyclone.model.api.marker.HasAttributes;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 12. 21.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
public class AttributeSet extends AttributeBase<Set<Attribute<?>>> implements HasAttributes {
  @NonNull
  private Set<Attribute<?>> value;

  public AttributeSet(@NonNull String name, @NonNull Set<Attribute<?>> value) {
    super(name);
    this.value = value;
  }

  @Override
  public Set<Attribute<?>> getAttributes() {
    return this.value;
  }

  @Override
  public void setAttributes(Set<Attribute<?>> attributes) {
    setValue(attributes);
  }

  public <T> Attribute<T> getAttribute(String name) {
    return HasAttributes.super.getAttribute(this.value, name);
  }
}
