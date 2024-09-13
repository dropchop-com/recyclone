package com.dropchop.recyclone.model.api.expr;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
public interface UnaryNode extends Node {
  Node getRight();
  void setRight(Node node);
}
