package com.dropchop.recyclone.model.api.expr.operand;

import com.dropchop.recyclone.model.api.expr.TextOperand;
import com.dropchop.recyclone.model.api.expr.impl.OperandBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 12. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Phrase extends OperandBase<List<Token>> implements TextOperand<List<Token>> {
}
