package com.dropchop.recyclone.base.api.model.expr.operand;

import com.dropchop.recyclone.base.api.model.expr.TextOperand;
import com.dropchop.recyclone.base.api.model.expr.impl.OperandBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Token extends OperandBase<String> implements TextOperand<String> {
  private boolean mixedCase = false;
  private boolean upperCase = false;

  @Override
  public String toString() {
    return "Token{" +
      "value=" + this.getParsedValue() +
      ", mixedCase=" + mixedCase +
      ", upperCase=" + upperCase +
      '}';
  }
}
