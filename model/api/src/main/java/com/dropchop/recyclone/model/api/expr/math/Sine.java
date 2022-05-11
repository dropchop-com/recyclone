package com.dropchop.recyclone.model.api.expr.math;

import com.dropchop.recyclone.model.api.expr.UnaryOperator;
import com.dropchop.recyclone.model.api.expr.impl.UnaryNodeBase;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 12. 21.
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
