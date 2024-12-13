package com.dropchop.recyclone.base.api.model.expr.impl;

import com.dropchop.recyclone.base.api.model.expr.Node;
import com.dropchop.recyclone.base.api.model.expr.UnaryNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UnaryNodeBase extends NodeBase implements UnaryNode {
  private Node right;
}
