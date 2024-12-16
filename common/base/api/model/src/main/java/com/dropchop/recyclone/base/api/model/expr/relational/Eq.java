package com.dropchop.recyclone.base.api.model.expr.relational;

import com.dropchop.recyclone.base.api.model.expr.RelationalOperator;
import com.dropchop.recyclone.base.api.model.expr.impl.BinaryNodeBase;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Eq extends BinaryNodeBase implements RelationalOperator {
  public Eq(@NonNull String value) {
    super();
    this.setValue(value);
  }
}
