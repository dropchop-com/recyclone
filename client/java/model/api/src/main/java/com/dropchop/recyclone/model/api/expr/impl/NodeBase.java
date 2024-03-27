package com.dropchop.recyclone.model.api.expr.impl;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.expr.BinaryTree;
import com.dropchop.recyclone.model.api.expr.Node;
import com.dropchop.recyclone.model.api.expr.Position;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
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
