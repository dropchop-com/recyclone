package com.dropchop.recyclone.model.api.expr.impl;

import com.dropchop.recyclone.model.api.expr.Node;
import com.dropchop.recyclone.model.api.expr.UnaryNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UnaryNodeBase extends NodeBase implements UnaryNode {
  private Node right;
}
