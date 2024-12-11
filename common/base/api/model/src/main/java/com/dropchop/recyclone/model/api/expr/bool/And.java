package com.dropchop.recyclone.model.api.expr.bool;

import com.dropchop.recyclone.model.api.expr.BinaryOperator;
import com.dropchop.recyclone.model.api.expr.impl.BinaryNodeBase;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class And extends BinaryNodeBase implements BinaryOperator {
  public And(@NonNull String value) {
    super();
    this.setValue(value);
  }
}
