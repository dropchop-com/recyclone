package com.dropchop.recyclone.base.api.model.expr.bool;

import com.dropchop.recyclone.base.api.model.expr.BinaryOperator;
import com.dropchop.recyclone.base.api.model.expr.impl.BinaryNodeBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Or extends BinaryNodeBase implements BinaryOperator {
  public Or(@NonNull String value) {
    super();
    this.setValue(value);
  }
}
