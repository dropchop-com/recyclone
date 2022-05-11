package com.dropchop.recyclone.model.api.expr.func;

import com.dropchop.recyclone.model.api.expr.UnaryLeafOperator;
import com.dropchop.recyclone.model.api.expr.impl.UnaryNodeBase;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 12. 21.
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
