package com.dropchop.recyclone.base.api.model.expr.math;

import com.dropchop.recyclone.base.api.model.expr.UnaryOperator;
import com.dropchop.recyclone.base.api.model.expr.impl.UnaryNodeBase;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 12. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Sine extends UnaryNodeBase implements UnaryOperator {
  public Sine(@NonNull String value) {
    super();
    this.setValue(value);
  }
}
