package com.dropchop.recyclone.model.api.expr.impl;

import com.dropchop.recyclone.model.api.expr.Operand;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class OperandBase<T> extends NodeBase implements Operand<T> {
  @EqualsAndHashCode.Exclude
  private T parsedValue;
}
