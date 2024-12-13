package com.dropchop.recyclone.base.api.model.expr.bool;

import com.dropchop.recyclone.base.api.model.expr.UnaryOperator;
import com.dropchop.recyclone.base.api.model.expr.impl.UnaryNodeBase;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Not extends UnaryNodeBase implements UnaryOperator {
  public Not(@NonNull String value) {
    super();
    this.setValue(value);
  }
}
