package com.dropchop.recyclone.base.api.model.expr.bool;

import com.dropchop.recyclone.base.api.model.expr.BinaryOperator;
import com.dropchop.recyclone.base.api.model.expr.impl.BinaryNodeBase;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Xor extends BinaryNodeBase implements BinaryOperator {
  public Xor(@NonNull String value) {
    super();
    this.setValue(value);
  }
}
