package com.dropchop.recyclone.model.api.expr.impl;

import com.dropchop.recyclone.model.api.expr.NamedOperand;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class NamedOperandBase<T> extends NodeBase implements NamedOperand<T> {
  private T parsedValue;
  private String name;
}
