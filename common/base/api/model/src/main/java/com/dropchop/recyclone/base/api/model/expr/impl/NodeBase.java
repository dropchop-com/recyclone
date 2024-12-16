package com.dropchop.recyclone.base.api.model.expr.impl;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.expr.BinaryTree;
import com.dropchop.recyclone.base.api.model.expr.Node;
import com.dropchop.recyclone.base.api.model.expr.Position;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Data
public abstract class NodeBase {

  private String id;
  private String value;

  @EqualsAndHashCode.Exclude
  private Position position;

  @EqualsAndHashCode.Exclude
  private transient BinaryTree tree;

  @EqualsAndHashCode.Exclude
  private transient Node parent;

  @EqualsAndHashCode.Exclude
  private Set<Attribute<?>> attributes;
}
