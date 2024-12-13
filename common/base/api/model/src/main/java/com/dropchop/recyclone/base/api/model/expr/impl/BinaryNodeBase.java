package com.dropchop.recyclone.base.api.model.expr.impl;

import com.dropchop.recyclone.base.api.model.expr.BinaryNode;
import com.dropchop.recyclone.base.api.model.expr.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BinaryNodeBase extends UnaryNodeBase implements BinaryNode {
  private Node left;
}
