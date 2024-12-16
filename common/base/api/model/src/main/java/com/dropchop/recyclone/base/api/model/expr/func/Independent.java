package com.dropchop.recyclone.base.api.model.expr.func;

import com.dropchop.recyclone.base.api.model.expr.UnaryLeafOperator;
import com.dropchop.recyclone.base.api.model.expr.impl.UnaryNodeBase;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 12. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Independent extends UnaryNodeBase implements UnaryLeafOperator {
  public Independent(@NonNull String value) {
    super();
    this.setValue(value);
  }
}
