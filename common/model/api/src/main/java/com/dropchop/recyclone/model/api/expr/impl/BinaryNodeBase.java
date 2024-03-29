package com.dropchop.recyclone.model.api.expr.impl;

import com.dropchop.recyclone.model.api.expr.BinaryNode;
import com.dropchop.recyclone.model.api.expr.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BinaryNodeBase extends UnaryNodeBase implements BinaryNode {
  private Node left;
}
