package com.dropchop.recyclone.model.api.expr.operand;

import com.dropchop.recyclone.model.api.expr.Operand;
import com.dropchop.recyclone.model.api.expr.impl.OperandBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Name extends OperandBase<String> {

  public static Name fromOperand(Operand<?> operand) {
    Name name = new Name();
    name.setAttributes(operand.getAttributes());
    name.setParent(operand.getParent());
    name.setId(operand.getId());
    name.setValue(operand.getValue());
    return name;
  }
}
