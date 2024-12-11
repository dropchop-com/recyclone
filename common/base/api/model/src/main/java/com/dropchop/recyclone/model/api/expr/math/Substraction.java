package com.dropchop.recyclone.model.api.expr.math;

import com.dropchop.recyclone.model.api.expr.BinaryOperator;
import com.dropchop.recyclone.model.api.expr.impl.BinaryNodeBase;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 12. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Substraction extends BinaryNodeBase implements BinaryOperator {
  public Substraction(@NonNull String value) {
    super();
    this.setValue(value);
  }
}
