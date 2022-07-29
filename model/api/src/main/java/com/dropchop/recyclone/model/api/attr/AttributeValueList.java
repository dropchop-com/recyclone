package com.dropchop.recyclone.model.api.attr;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 25. 07. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AttributeValueList<T> extends AttributeBase<List<T>> {
  @NonNull
  private List<T> value;

  public AttributeValueList(@NonNull String name, @NonNull List<T> value) {
    super(name);
    this.value = value;
  }
}
