package com.dropchop.recyclone.base.api.model.attr;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 25. 07. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
public class AttributeValueList<X> extends AttributeBase<List<X>> {
  @NonNull
  private List<X> value;

  public AttributeValueList(@NonNull String name, @NonNull List<X> value) {
    super(name);
    this.value = value;
  }
}
