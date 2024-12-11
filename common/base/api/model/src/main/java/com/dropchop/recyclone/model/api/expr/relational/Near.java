package com.dropchop.recyclone.model.api.expr.relational;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.attr.AttributeDecimal;
import com.dropchop.recyclone.model.api.expr.BinaryLeafOperator;
import com.dropchop.recyclone.model.api.expr.impl.BinaryNodeBase;
import com.dropchop.recyclone.model.api.expr.ReservedSymbols;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Near extends BinaryNodeBase implements BinaryLeafOperator {
  private int distance = 1;

  public Near(@NonNull String value) {
    super();
    this.setValue(value);
  }

  public void setAttributes(Set<Attribute<?>> attributes) {
    for (Attribute<?> attribute : attributes) {
      String name = attribute.getName();
      if (name == null || name.isEmpty()) {
        continue;
      }
      if (name.startsWith(ReservedSymbols.DEFAULT_ATTR_NAME)) {
        if (attribute instanceof AttributeDecimal) {
          BigDecimal value = ((AttributeDecimal)attribute).getValue();
          this.setDistance(value.intValue());
        }
      }
    }
    super.setAttributes(attributes);
  }
}
