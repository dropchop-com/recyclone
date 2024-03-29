package com.dropchop.recyclone.model.api.expr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 12. 21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {
  private int linePos = 0;
  private int absPos = 0;
  private int lineNum = 0;
}
