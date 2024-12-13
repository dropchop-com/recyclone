package com.dropchop.recyclone.base.api.model.expr.math;

import com.dropchop.recyclone.base.api.model.expr.BinaryOperator;
import com.dropchop.recyclone.base.api.model.expr.impl.BinaryNodeBase;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 12. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Power extends BinaryNodeBase implements BinaryOperator {
  public Power(@NonNull String value) {
    super();
    this.setValue(value);
  }
}
