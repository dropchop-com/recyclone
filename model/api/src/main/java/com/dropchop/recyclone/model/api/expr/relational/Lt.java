package com.dropchop.recyclone.model.api.expr.relational;

import com.dropchop.recyclone.model.api.expr.RelationalOperator;
import com.dropchop.recyclone.model.api.expr.impl.BinaryNodeBase;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Lt extends BinaryNodeBase implements RelationalOperator {
  public Lt(@NonNull String value) {
    super();
    this.setValue(value);
  }
}
