package com.dropchop.recyclone.base.api.model.expr.impl;

import com.dropchop.recyclone.base.api.model.expr.NamedOperand;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public abstract class NamedOperandBase<T> extends NodeBase implements NamedOperand<T> {
  private T parsedValue;
  private String name;
}
