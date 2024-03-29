package com.dropchop.recyclone.model.api.expr.operand;

import com.dropchop.recyclone.model.api.expr.impl.OperandBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 12. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class DateTime extends OperandBase<ZonedDateTime> {
  public DateTime(String value, ZonedDateTime date) {
    this.setParsedValue(date);
    this.setValue(value);
  }
}
