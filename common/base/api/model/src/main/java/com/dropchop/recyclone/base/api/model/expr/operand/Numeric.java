package com.dropchop.recyclone.base.api.model.expr.operand;

import com.dropchop.recyclone.base.api.model.expr.impl.OperandBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Numeric extends OperandBase<Double> {
}
