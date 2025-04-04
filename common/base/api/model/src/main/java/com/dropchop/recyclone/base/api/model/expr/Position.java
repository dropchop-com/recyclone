package com.dropchop.recyclone.base.api.model.expr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 12. 21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {
  private int linePos = 0;
  private int absPos = 0;
  private int lineNum = 0;
}
